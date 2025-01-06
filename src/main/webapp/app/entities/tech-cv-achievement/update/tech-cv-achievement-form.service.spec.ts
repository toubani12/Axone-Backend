import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-achievement.test-samples';

import { TechCVAchievementFormService } from './tech-cv-achievement-form.service';

describe('TechCVAchievement Form Service', () => {
  let service: TechCVAchievementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVAchievementFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVAchievementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVAchievementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            achievement: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVAchievement should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVAchievementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            achievement: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVAchievement', () => {
      it('should return NewTechCVAchievement for default TechCVAchievement initial value', () => {
        const formGroup = service.createTechCVAchievementFormGroup(sampleWithNewData);

        const techCVAchievement = service.getTechCVAchievement(formGroup) as any;

        expect(techCVAchievement).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVAchievement for empty TechCVAchievement initial value', () => {
        const formGroup = service.createTechCVAchievementFormGroup();

        const techCVAchievement = service.getTechCVAchievement(formGroup) as any;

        expect(techCVAchievement).toMatchObject({});
      });

      it('should return ITechCVAchievement', () => {
        const formGroup = service.createTechCVAchievementFormGroup(sampleWithRequiredData);

        const techCVAchievement = service.getTechCVAchievement(formGroup) as any;

        expect(techCVAchievement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVAchievement should not enable id FormControl', () => {
        const formGroup = service.createTechCVAchievementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVAchievement should disable id FormControl', () => {
        const formGroup = service.createTechCVAchievementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
