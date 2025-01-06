import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVAchievementService } from '../service/tech-cv-achievement.service';
import { ITechCVAchievement } from '../tech-cv-achievement.model';
import { TechCVAchievementFormService } from './tech-cv-achievement-form.service';

import { TechCVAchievementUpdateComponent } from './tech-cv-achievement-update.component';

describe('TechCVAchievement Management Update Component', () => {
  let comp: TechCVAchievementUpdateComponent;
  let fixture: ComponentFixture<TechCVAchievementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let techCVAchievementFormService: TechCVAchievementFormService;
  let techCVAchievementService: TechCVAchievementService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVAchievementUpdateComponent],
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
      .overrideTemplate(TechCVAchievementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechCVAchievementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    techCVAchievementFormService = TestBed.inject(TechCVAchievementFormService);
    techCVAchievementService = TestBed.inject(TechCVAchievementService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TechnicalCV query and add missing value', () => {
      const techCVAchievement: ITechCVAchievement = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 28424 };
      techCVAchievement.technicalCV = technicalCV;

      const technicalCVCollection: ITechnicalCV[] = [{ id: 31900 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: technicalCVCollection })));
      const additionalTechnicalCVS = [technicalCV];
      const expectedCollection: ITechnicalCV[] = [...additionalTechnicalCVS, ...technicalCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ techCVAchievement });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(
        technicalCVCollection,
        ...additionalTechnicalCVS.map(expect.objectContaining),
      );
      expect(comp.technicalCVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const techCVAchievement: ITechCVAchievement = { id: 456 };
      const technicalCV: ITechnicalCV = { id: 27990 };
      techCVAchievement.technicalCV = technicalCV;

      activatedRoute.data = of({ techCVAchievement });
      comp.ngOnInit();

      expect(comp.technicalCVSSharedCollection).toContain(technicalCV);
      expect(comp.techCVAchievement).toEqual(techCVAchievement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAchievement>>();
      const techCVAchievement = { id: 123 };
      jest.spyOn(techCVAchievementFormService, 'getTechCVAchievement').mockReturnValue(techCVAchievement);
      jest.spyOn(techCVAchievementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAchievement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVAchievement }));
      saveSubject.complete();

      // THEN
      expect(techCVAchievementFormService.getTechCVAchievement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(techCVAchievementService.update).toHaveBeenCalledWith(expect.objectContaining(techCVAchievement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAchievement>>();
      const techCVAchievement = { id: 123 };
      jest.spyOn(techCVAchievementFormService, 'getTechCVAchievement').mockReturnValue({ id: null });
      jest.spyOn(techCVAchievementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAchievement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: techCVAchievement }));
      saveSubject.complete();

      // THEN
      expect(techCVAchievementFormService.getTechCVAchievement).toHaveBeenCalled();
      expect(techCVAchievementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechCVAchievement>>();
      const techCVAchievement = { id: 123 };
      jest.spyOn(techCVAchievementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ techCVAchievement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(techCVAchievementService.update).toHaveBeenCalled();
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
