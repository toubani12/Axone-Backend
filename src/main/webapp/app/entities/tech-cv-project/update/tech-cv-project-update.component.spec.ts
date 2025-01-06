import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVProjectService } from '../service/tech-cv-project.service';
import { ITechCVProject } from '../tech-cv-project.model';
import { TechCVProjectFormService } from './tech-cv-project-form.service';

import { TechCVProjectUpdateComponent } from './tech-cv-project-update.component';

describe('TechCVProject Management Update Component', () => {
  let comp: TechCVProjectUpdateComponent;
  let fixture: ComponentFixture<TechCVProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVProjectFormService: TechCVProjectFormService;
  let techCVProjectService: TechCVProjectService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVProjectUpdateComponent],
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
      .overrideTemplate(TechCVProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVProjectFormService = TestBed.inject(TechCVProjectFormService);
    techCVProjectService = TestBed.inject(TechCVProjectService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVProject: ITechCVProject = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 1917 };
      techCVProject.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 31391 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVProject });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVProject: ITechCVProject = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 149 };
      techCVProject.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVProject });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVProject).toEqual(techCVProject);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVProject>>();
      const techCVProject = { id: 123 };
      jest.spyOn(techCVProjectFormService, 'getTechCVProject').mockReturnValue(techCVProject);
      jest.spyOn(techCVProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVProject }));
      saveSubject.complete();

      // THEN
      expect(techCVProjectFormService.getTechCVProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVProjectService.update).toHaveBeenCalledWith(expect.objectContaining(techCVProject));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVProject>>();
      const techCVProject = { id: 123 };
      jest.spyOn(techCVProjectFormService, 'getTechCVProject').mockReturnValue({ id: null });
      jest.spyOn(techCVProjectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVProject: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVProject }));
      saveSubject.complete();

      // THEN
      expect(techCVProjectFormService.getTechCVProject).toHaveBeenCalled();
      expect(techCVProjectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVProject>>();
      const techCVProject = { id: 123 };
      jest.spyOn(techCVProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVProjectService.update).toHaveBeenCalled();
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
