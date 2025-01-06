import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVSoftSkillsService } from '../service/tech-cv-soft-skills.service';
import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { TechCVSoftSkillsFormService } from './tech-cv-soft-skills-form.service';

import { TechCVSoftSkillsUpdateComponent } from './tech-cv-soft-skills-update.component';

describe('TechCVSoftSkills Management Update Component', () => {
  let comp: TechCVSoftSkillsUpdateComponent;
  let fixture: ComponentFixture<TechCVSoftSkillsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVSoftSkillsFormService: TechCVSoftSkillsFormService;
  let techCVSoftSkillsService: TechCVSoftSkillsService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVSoftSkillsUpdateComponent],
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
      .overrideTemplate(TechCVSoftSkillsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVSoftSkillsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVSoftSkillsFormService = TestBed.inject(TechCVSoftSkillsFormService);
    techCVSoftSkillsService = TestBed.inject(TechCVSoftSkillsService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVSoftSkills: ITechCVSoftSkills = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 18971 };
      techCVSoftSkills.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 20237 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVSoftSkills });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVSoftSkills: ITechCVSoftSkills = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 2252 };
      techCVSoftSkills.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVSoftSkills });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVSoftSkills).toEqual(techCVSoftSkills);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVSoftSkills>>();
      const techCVSoftSkills = { id: 123 };
      jest.spyOn(techCVSoftSkillsFormService, 'getTechCVSoftSkills').mockReturnValue(techCVSoftSkills);
      jest.spyOn(techCVSoftSkillsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVSoftSkills });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVSoftSkills }));
      saveSubject.complete();

      // THEN
      expect(techCVSoftSkillsFormService.getTechCVSoftSkills).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVSoftSkillsService.update).toHaveBeenCalledWith(expect.objectContaining(techCVSoftSkills));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVSoftSkills>>();
      const techCVSoftSkills = { id: 123 };
      jest.spyOn(techCVSoftSkillsFormService, 'getTechCVSoftSkills').mockReturnValue({ id: null });
      jest.spyOn(techCVSoftSkillsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVSoftSkills: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVSoftSkills }));
      saveSubject.complete();

      // THEN
      expect(techCVSoftSkillsFormService.getTechCVSoftSkills).toHaveBeenCalled();
      expect(techCVSoftSkillsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVSoftSkills>>();
      const techCVSoftSkills = { id: 123 };
      jest.spyOn(techCVSoftSkillsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVSoftSkills });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVSoftSkillsService.update).toHaveBeenCalled();
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
