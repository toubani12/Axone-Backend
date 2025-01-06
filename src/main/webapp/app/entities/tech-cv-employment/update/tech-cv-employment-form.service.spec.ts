import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-employment.test-samples';

import { TechCVEmploymentFormService } from './tech-cv-employment-form.service';

describe('TechCVEmployment Form Service', () => {
  let service: TechCVEmploymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVEmploymentFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVEmploymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVEmploymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employment: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVEmployment should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVEmploymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employment: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVEmployment', () => {
      it('should return NewTechCVEmployment for default TechCVEmployment initial value', () => {
        const formGroup = service.createTechCVEmploymentFormGroup(sampleWithNewData);

        const techCVEmployment = service.getTechCVEmployment(formGroup) as any;

        expect(techCVEmployment).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVEmployment for empty TechCVEmployment initial value', () => {
        const formGroup = service.createTechCVEmploymentFormGroup();

        const techCVEmployment = service.getTechCVEmployment(formGroup) as any;

        expect(techCVEmployment).toMatchObject({});
      });

      it('should return ITechCVEmployment', () => {
        const formGroup = service.createTechCVEmploymentFormGroup(sampleWithRequiredData);

        const techCVEmployment = service.getTechCVEmployment(formGroup) as any;

        expect(techCVEmployment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVEmployment should not enable id FormControl', () => {
        const formGroup = service.createTechCVEmploymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVEmployment should disable id FormControl', () => {
        const formGroup = service.createTechCVEmploymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
