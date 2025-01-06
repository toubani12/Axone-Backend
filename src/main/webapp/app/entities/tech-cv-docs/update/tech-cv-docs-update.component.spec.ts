import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVDocsService } from '../service/tech-cv-docs.service';
import { ITechCVDocs } from '../tech-cv-docs.model';
import { TechCVDocsFormService } from './tech-cv-docs-form.service';

import { TechCVDocsUpdateComponent } from './tech-cv-docs-update.component';

describe('TechCVDocs Management Update Component', () => {
  let comp: TechCVDocsUpdateComponent;
  let fixture: ComponentFixture<TechCVDocsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVDocsFormService: TechCVDocsFormService;
  let techCVDocsService: TechCVDocsService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVDocsUpdateComponent],
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
      .overrideTemplate(TechCVDocsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVDocsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVDocsFormService = TestBed.inject(TechCVDocsFormService);
    techCVDocsService = TestBed.inject(TechCVDocsService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVDocs: ITechCVDocs = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 29878 };
      techCVDocs.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 6882 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVDocs });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVDocs: ITechCVDocs = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 229 };
      techCVDocs.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVDocs });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVDocs).toEqual(techCVDocs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVDocs>>();
      const techCVDocs = { id: 123 };
      jest.spyOn(techCVDocsFormService, 'getTechCVDocs').mockReturnValue(techCVDocs);
      jest.spyOn(techCVDocsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVDocs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVDocs }));
      saveSubject.complete();

      // THEN
      expect(techCVDocsFormService.getTechCVDocs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVDocsService.update).toHaveBeenCalledWith(expect.objectContaining(techCVDocs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVDocs>>();
      const techCVDocs = { id: 123 };
      jest.spyOn(techCVDocsFormService, 'getTechCVDocs').mockReturnValue({ id: null });
      jest.spyOn(techCVDocsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVDocs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVDocs }));
      saveSubject.complete();

      // THEN
      expect(techCVDocsFormService.getTechCVDocs).toHaveBeenCalled();
      expect(techCVDocsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVDocs>>();
      const techCVDocs = { id: 123 };
      jest.spyOn(techCVDocsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVDocs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVDocsService.update).toHaveBeenCalled();
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
