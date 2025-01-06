import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../candidate.test-samples';

import { CandidateFormService } from './candidate-form.service';

describe('Candidate Form Service', () => {
  let service: CandidateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CandidateFormService);
  });

  describe('Service methods', () => {
    describe('createCandidateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCandidateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            linkedinUrl: expect.any(Object),
            fullName: expect.any(Object),
            yearsOfExperience: expect.any(Object),
            currentSalary: expect.any(Object),
            desiredSalary: expect.any(Object),
            hasContract: expect.any(Object),
            hired: expect.any(Object),
            rate: expect.any(Object),
            techCV: expect.any(Object),
            domains: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });

      it('passing ICandidate should create a new form with FormGroup', () => {
        const formGroup = service.createCandidateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            linkedinUrl: expect.any(Object),
            fullName: expect.any(Object),
            yearsOfExperience: expect.any(Object),
            currentSalary: expect.any(Object),
            desiredSalary: expect.any(Object),
            hasContract: expect.any(Object),
            hired: expect.any(Object),
            rate: expect.any(Object),
            techCV: expect.any(Object),
            domains: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });
    });

    describe('getCandidate', () => {
      it('should return NewCandidate for default Candidate initial value', () => {
        const formGroup = service.createCandidateFormGroup(sampleWithNewData);

        const candidate = service.getCandidate(formGroup) as any;

        expect(candidate).toMatchObject(sampleWithNewData);
      });

      it('should return NewCandidate for empty Candidate initial value', () => {
        const formGroup = service.createCandidateFormGroup();

        const candidate = service.getCandidate(formGroup) as any;

        expect(candidate).toMatchObject({});
      });

      it('should return ICandidate', () => {
        const formGroup = service.createCandidateFormGroup(sampleWithRequiredData);

        const candidate = service.getCandidate(formGroup) as any;

        expect(candidate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICandidate should not enable id FormControl', () => {
        const formGroup = service.createCandidateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCandidate should disable id FormControl', () => {
        const formGroup = service.createCandidateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
