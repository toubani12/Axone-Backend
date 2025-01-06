import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVHardSkillsService } from '../service/tech-cv-hard-skills.service';
import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';
import { TechCVHardSkillsFormService } from './tech-cv-hard-skills-form.service';

import { TechCVHardSkillsUpdateComponent } from './tech-cv-hard-skills-update.component';

describe('TechCVHardSkills Management Update Component', () => {
  let comp: TechCVHardSkillsUpdateComponent;
  let fixture: ComponentFixture<TechCVHardSkillsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVHardSkillsFormService: TechCVHardSkillsFormService;
  let techCVHardSkillsService: TechCVHardSkillsService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVHardSkillsUpdateComponent],
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
      .overrideTemplate(TechCVHardSkillsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVHardSkillsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVHardSkillsFormService = TestBed.inject(TechCVHardSkillsFormService);
    techCVHardSkillsService = TestBed.inject(TechCVHardSkillsService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVHardSkills: ITechCVHardSkills = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 22167 };
      techCVHardSkills.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 732 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVHardSkills });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVHardSkills: ITechCVHardSkills = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 32040 };
      techCVHardSkills.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVHardSkills });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVHardSkills).toEqual(techCVHardSkills);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVHardSkills>>();
      const techCVHardSkills = { id: 123 };
      jest.spyOn(techCVHardSkillsFormService, 'getTechCVHardSkills').mockReturnValue(techCVHardSkills);
      jest.spyOn(techCVHardSkillsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVHardSkills });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVHardSkills }));
      saveSubject.complete();

      // THEN
      expect(techCVHardSkillsFormService.getTechCVHardSkills).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVHardSkillsService.update).toHaveBeenCalledWith(expect.objectContaining(techCVHardSkills));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVHardSkills>>();
      const techCVHardSkills = { id: 123 };
      jest.spyOn(techCVHardSkillsFormService, 'getTechCVHardSkills').mockReturnValue({ id: null });
      jest.spyOn(techCVHardSkillsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVHardSkills: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVHardSkills }));
      saveSubject.complete();

      // THEN
      expect(techCVHardSkillsFormService.getTechCVHardSkills).toHaveBeenCalled();
      expect(techCVHardSkillsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVHardSkills>>();
      const techCVHardSkills = { id: 123 };
      jest.spyOn(techCVHardSkillsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVHardSkills });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVHardSkillsService.update).toHaveBeenCalled();
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
