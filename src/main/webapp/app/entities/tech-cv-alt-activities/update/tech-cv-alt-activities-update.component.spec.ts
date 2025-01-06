import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVAltActivitiesService } from '../service/tech-cv-alt-activities.service';
import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';
import { TechCVAltActivitiesFormService } from './tech-cv-alt-activities-form.service';

import { TechCVAltActivitiesUpdateComponent } from './tech-cv-alt-activities-update.component';

describe('TechCVAltActivities Management Update Component', () => {
  let comp: TechCVAltActivitiesUpdateComponent;
  let fixture: ComponentFixture<TechCVAltActivitiesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVAltActivitiesFormService: TechCVAltActivitiesFormService;
  let techCVAltActivitiesService: TechCVAltActivitiesService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVAltActivitiesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TechCVAltActivitiesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVAltActivitiesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVAltActivitiesFormService = TestBed.inject(TechCVAltActivitiesFormService);
    techCVAltActivitiesService = TestBed.inject(TechCVAltActivitiesService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVAltActivities: ITechCVAltActivities = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 21731 };
      techCVAltActivities.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 14697 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVAltActivities });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVAltActivities: ITechCVAltActivities = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 1541 };
      techCVAltActivities.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVAltActivities });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVAltActivities).toEqual(techCVAltActivities);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAltActivities>>();
      const techCVAltActivities = { id: 123 };
      jest.spyOn(techCVAltActivitiesFormService, 'getTechCVAltActivities').mockReturnValue(techCVAltActivities);
      jest.spyOn(techCVAltActivitiesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAltActivities });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVAltActivities }));
      saveSubject.complete();

      // THEN
      expect(techCVAltActivitiesFormService.getTechCVAltActivities).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVAltActivitiesService.update).toHaveBeenCalledWith(expect.objectContaining(techCVAltActivities));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAltActivities>>();
      const techCVAltActivities = { id: 123 };
      jest.spyOn(techCVAltActivitiesFormService, 'getTechCVAltActivities').mockReturnValue({ id: null });
      jest.spyOn(techCVAltActivitiesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAltActivities: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVAltActivities }));
      saveSubject.complete();

      // THEN
      expect(techCVAltActivitiesFormService.getTechCVAltActivities).toHaveBeenCalled();
      expect(techCVAltActivitiesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAltActivities>>();
      const techCVAltActivities = { id: 123 };
      jest.spyOn(techCVAltActivitiesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAltActivities });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVAltActivitiesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTechnicalCV', () => {
      it('Should forward to technicalCVService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(technicalCVService, 'compareTechnicalCV');
        comp.compareTechnicalCV(entity, entity2);
        expect(technicalCVService.compareTechnicalCV).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
