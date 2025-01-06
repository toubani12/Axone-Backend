import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-alt-activities.test-samples';

import { TechCVAltActivitiesFormService } from './tech-cv-alt-activities-form.service';

describe('TechCVAltActivities Form Service', () => {
  let service: TechCVAltActivitiesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVAltActivitiesFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVAltActivitiesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            activities: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVAltActivities should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            activities: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVAltActivities', () => {
      it('should return NewTechCVAltActivities for default TechCVAltActivities initial value', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup(sampleWithNewData);

        const techCVAltActivities = service.getTechCVAltActivities(formGroup) as any;

        expect(techCVAltActivities).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVAltActivities for empty TechCVAltActivities initial value', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup();

        const techCVAltActivities = service.getTechCVAltActivities(formGroup) as any;

        expect(techCVAltActivities).toMatchObject({});
      });

      it('should return ITechCVAltActivities', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup(sampleWithRequiredData);

        const techCVAltActivities = service.getTechCVAltActivities(formGroup) as any;

        expect(techCVAltActivities).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVAltActivities should not enable id FormControl', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVAltActivities should disable id FormControl', () => {
        const formGroup = service.createTechCVAltActivitiesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
