import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../technical-cv.test-samples';

import { TechnicalCVFormService } from './technical-cv-form.service';

describe('TechnicalCV Form Service', () => {
  let service: TechnicalCVFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechnicalCVFormService);
  });

  describe('Service methods', () => {
    describe('createTechnicalCVFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechnicalCVFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            level: expect.any(Object),
            profileDescription: expect.any(Object),
          }),
        );
      });

      it('passing ITechnicalCV should create a new form with FormGroup', () => {
        const formGroup = service.createTechnicalCVFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            level: expect.any(Object),
            profileDescription: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechnicalCV', () => {
      it('should return NewTechnicalCV for default TechnicalCV initial value', () => {
        const formGroup = service.createTechnicalCVFormGroup(sampleWithNewData);

        const technicalCV = service.getTechnicalCV(formGroup) as any;

        expect(technicalCV).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechnicalCV for empty TechnicalCV initial value', () => {
        const formGroup = service.createTechnicalCVFormGroup();

        const technicalCV = service.getTechnicalCV(formGroup) as any;

        expect(technicalCV).toMatchObject({});
      });

      it('should return ITechnicalCV', () => {
        const formGroup = service.createTechnicalCVFormGroup(sampleWithRequiredData);

        const technicalCV = service.getTechnicalCV(formGroup) as any;

        expect(technicalCV).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechnicalCV should not enable id FormControl', () => {
        const formGroup = service.createTechnicalCVFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechnicalCV should disable id FormControl', () => {
        const formGroup = service.createTechnicalCVFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
