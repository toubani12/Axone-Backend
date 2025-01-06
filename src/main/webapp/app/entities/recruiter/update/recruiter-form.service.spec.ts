import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../recruiter.test-samples';

import { RecruiterFormService } from './recruiter-form.service';

describe('Recruiter Form Service', () => {
  let service: RecruiterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecruiterFormService);
  });

  describe('Service methods', () => {
    describe('createRecruiterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecruiterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            profileImage: expect.any(Object),
            address: expect.any(Object),
            role: expect.any(Object),
            status: expect.any(Object),
            name: expect.any(Object),
            label: expect.any(Object),
            linkedinUrl: expect.any(Object),
            approvedExperience: expect.any(Object),
            score: expect.any(Object),
            relatedUser: expect.any(Object),
            wallet: expect.any(Object),
            applications: expect.any(Object),
            operationalDomains: expect.any(Object),
          }),
        );
      });

      it('passing IRecruiter should create a new form with FormGroup', () => {
        const formGroup = service.createRecruiterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            profileImage: expect.any(Object),
            address: expect.any(Object),
            role: expect.any(Object),
            status: expect.any(Object),
            name: expect.any(Object),
            label: expect.any(Object),
            linkedinUrl: expect.any(Object),
            approvedExperience: expect.any(Object),
            score: expect.any(Object),
            relatedUser: expect.any(Object),
            wallet: expect.any(Object),
            applications: expect.any(Object),
            operationalDomains: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecruiter', () => {
      it('should return NewRecruiter for default Recruiter initial value', () => {
        const formGroup = service.createRecruiterFormGroup(sampleWithNewData);

        const recruiter = service.getRecruiter(formGroup) as any;

        expect(recruiter).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecruiter for empty Recruiter initial value', () => {
        const formGroup = service.createRecruiterFormGroup();

        const recruiter = service.getRecruiter(formGroup) as any;

        expect(recruiter).toMatchObject({});
      });

      it('should return IRecruiter', () => {
        const formGroup = service.createRecruiterFormGroup(sampleWithRequiredData);

        const recruiter = service.getRecruiter(formGroup) as any;

        expect(recruiter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecruiter should not enable id FormControl', () => {
        const formGroup = service.createRecruiterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecruiter should disable id FormControl', () => {
        const formGroup = service.createRecruiterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
