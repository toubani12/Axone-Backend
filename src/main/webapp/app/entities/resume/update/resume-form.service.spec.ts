import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resume.test-samples';

import { ResumeFormService } from './resume-form.service';

describe('Resume Form Service', () => {
  let service: ResumeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResumeFormService);
  });

  describe('Service methods', () => {
    describe('createResumeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResumeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            resume: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });

      it('passing IResume should create a new form with FormGroup', () => {
        const formGroup = service.createResumeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            resume: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });
    });

    describe('getResume', () => {
      it('should return NewResume for default Resume initial value', () => {
        const formGroup = service.createResumeFormGroup(sampleWithNewData);

        const resume = service.getResume(formGroup) as any;

        expect(resume).toMatchObject(sampleWithNewData);
      });

      it('should return NewResume for empty Resume initial value', () => {
        const formGroup = service.createResumeFormGroup();

        const resume = service.getResume(formGroup) as any;

        expect(resume).toMatchObject({});
      });

      it('should return IResume', () => {
        const formGroup = service.createResumeFormGroup(sampleWithRequiredData);

        const resume = service.getResume(formGroup) as any;

        expect(resume).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResume should not enable id FormControl', () => {
        const formGroup = service.createResumeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResume should disable id FormControl', () => {
        const formGroup = service.createResumeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
