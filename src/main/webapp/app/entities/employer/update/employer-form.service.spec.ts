import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employer.test-samples';

import { EmployerFormService } from './employer-form.service';

describe('Employer Form Service', () => {
  let service: EmployerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployerFormService);
  });

  describe('Service methods', () => {
    describe('createEmployerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployerFormGroup();

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
            relatedUser: expect.any(Object),
            wallet: expect.any(Object),
          }),
        );
      });

      it('passing IEmployer should create a new form with FormGroup', () => {
        const formGroup = service.createEmployerFormGroup(sampleWithRequiredData);

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
            relatedUser: expect.any(Object),
            wallet: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmployer', () => {
      it('should return NewEmployer for default Employer initial value', () => {
        const formGroup = service.createEmployerFormGroup(sampleWithNewData);

        const employer = service.getEmployer(formGroup) as any;

        expect(employer).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployer for empty Employer initial value', () => {
        const formGroup = service.createEmployerFormGroup();

        const employer = service.getEmployer(formGroup) as any;

        expect(employer).toMatchObject({});
      });

      it('should return IEmployer', () => {
        const formGroup = service.createEmployerFormGroup(sampleWithRequiredData);

        const employer = service.getEmployer(formGroup) as any;

        expect(employer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployer should not enable id FormControl', () => {
        const formGroup = service.createEmployerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployer should disable id FormControl', () => {
        const formGroup = service.createEmployerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
