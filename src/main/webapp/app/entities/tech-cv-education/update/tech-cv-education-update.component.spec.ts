import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVEducationService } from '../service/tech-cv-education.service';
import { ITechCVEducation } from '../tech-cv-education.model';
import { TechCVEducationFormService } from './tech-cv-education-form.service';

import { TechCVEducationUpdateComponent } from './tech-cv-education-update.component';

describe('TechCVEducation Management Update Component', () => {
  let comp: TechCVEducationUpdateComponent;
  let fixture: ComponentFixture<TechCVEducationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVEducationFormService: TechCVEducationFormService;
  let techCVEducationService: TechCVEducationService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVEducationUpdateComponent],
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
      .overrideTemplate(TechCVEducationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVEducationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVEducationFormService = TestBed.inject(TechCVEducationFormService);
    techCVEducationService = TestBed.inject(TechCVEducationService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVEducation: ITechCVEducation = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 15317 };
      techCVEducation.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 28570 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVEducation });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVEducation: ITechCVEducation = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 15947 };
      techCVEducation.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVEducation });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVEducation).toEqual(techCVEducation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEducation>>();
      const techCVEducation = { id: 123 };
      jest.spyOn(techCVEducationFormService, 'getTechCVEducation').mockReturnValue(techCVEducation);
      jest.spyOn(techCVEducationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEducation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVEducation }));
      saveSubject.complete();

      // THEN
      expect(techCVEducationFormService.getTechCVEducation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVEducationService.update).toHaveBeenCalledWith(expect.objectContaining(techCVEducation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEducation>>();
      const techCVEducation = { id: 123 };
      jest.spyOn(techCVEducationFormService, 'getTechCVEducation').mockReturnValue({ id: null });
      jest.spyOn(techCVEducationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEducation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVEducation }));
      saveSubject.complete();

      // THEN
      expect(techCVEducationFormService.getTechCVEducation).toHaveBeenCalled();
      expect(techCVEducationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEducation>>();
      const techCVEducation = { id: 123 };
      jest.spyOn(techCVEducationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEducation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVEducationService.update).toHaveBeenCalled();
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
