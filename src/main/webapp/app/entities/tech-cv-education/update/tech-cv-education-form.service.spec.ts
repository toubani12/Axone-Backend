import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-education.test-samples';

import { TechCVEducationFormService } from './tech-cv-education-form.service';

describe('TechCVEducation Form Service', () => {
  let service: TechCVEducationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVEducationFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVEducationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVEducationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            education: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVEducation should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVEducationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            education: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVEducation', () => {
      it('should return NewTechCVEducation for default TechCVEducation initial value', () => {
        const formGroup = service.createTechCVEducationFormGroup(sampleWithNewData);

        const techCVEducation = service.getTechCVEducation(formGroup) as any;

        expect(techCVEducation).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVEducation for empty TechCVEducation initial value', () => {
        const formGroup = service.createTechCVEducationFormGroup();

        const techCVEducation = service.getTechCVEducation(formGroup) as any;

        expect(techCVEducation).toMatchObject({});
      });

      it('should return ITechCVEducation', () => {
        const formGroup = service.createTechCVEducationFormGroup(sampleWithRequiredData);

        const techCVEducation = service.getTechCVEducation(formGroup) as any;

        expect(techCVEducation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVEducation should not enable id FormControl', () => {
        const formGroup = service.createTechCVEducationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVEducation should disable id FormControl', () => {
        const formGroup = service.createTechCVEducationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
