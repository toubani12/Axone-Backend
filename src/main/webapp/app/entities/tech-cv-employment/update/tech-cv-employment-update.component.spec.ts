import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVEmploymentService } from '../service/tech-cv-employment.service';
import { ITechCVEmployment } from '../tech-cv-employment.model';
import { TechCVEmploymentFormService } from './tech-cv-employment-form.service';

import { TechCVEmploymentUpdateComponent } from './tech-cv-employment-update.component';

describe('TechCVEmployment Management Update Component', () => {
  let comp: TechCVEmploymentUpdateComponent;
  let fixture: ComponentFixture<TechCVEmploymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVEmploymentFormService: TechCVEmploymentFormService;
  let techCVEmploymentService: TechCVEmploymentService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVEmploymentUpdateComponent],
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
      .overrideTemplate(TechCVEmploymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVEmploymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVEmploymentFormService = TestBed.inject(TechCVEmploymentFormService);
    techCVEmploymentService = TestBed.inject(TechCVEmploymentService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVEmployment: ITechCVEmployment = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 1427 };
      techCVEmployment.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 5378 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVEmployment });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVEmployment: ITechCVEmployment = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 10261 };
      techCVEmployment.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVEmployment });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVEmployment).toEqual(techCVEmployment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEmployment>>();
      const techCVEmployment = { id: 123 };
      jest.spyOn(techCVEmploymentFormService, 'getTechCVEmployment').mockReturnValue(techCVEmployment);
      jest.spyOn(techCVEmploymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEmployment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVEmployment }));
      saveSubject.complete();

      // THEN
      expect(techCVEmploymentFormService.getTechCVEmployment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVEmploymentService.update).toHaveBeenCalledWith(expect.objectContaining(techCVEmployment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEmployment>>();
      const techCVEmployment = { id: 123 };
      jest.spyOn(techCVEmploymentFormService, 'getTechCVEmployment').mockReturnValue({ id: null });
      jest.spyOn(techCVEmploymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEmployment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVEmployment }));
      saveSubject.complete();

      // THEN
      expect(techCVEmploymentFormService.getTechCVEmployment).toHaveBeenCalled();
      expect(techCVEmploymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVEmployment>>();
      const techCVEmployment = { id: 123 };
      jest.spyOn(techCVEmploymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVEmployment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVEmploymentService.update).toHaveBeenCalled();
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
