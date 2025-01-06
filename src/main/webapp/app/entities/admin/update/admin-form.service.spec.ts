import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../admin.test-samples';

import { AdminFormService } from './admin-form.service';

describe('Admin Form Service', () => {
  let service: AdminFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminFormService);
  });

  describe('Service methods', () => {
    describe('createAdminFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdminFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            systemName: expect.any(Object),
            relatedUser: expect.any(Object),
            systemWallet: expect.any(Object),
          }),
        );
      });

      it('passing IAdmin should create a new form with FormGroup', () => {
        const formGroup = service.createAdminFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            systemName: expect.any(Object),
            relatedUser: expect.any(Object),
            systemWallet: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdmin', () => {
      it('should return NewAdmin for default Admin initial value', () => {
        const formGroup = service.createAdminFormGroup(sampleWithNewData);

        const admin = service.getAdmin(formGroup) as any;

        expect(admin).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdmin for empty Admin initial value', () => {
        const formGroup = service.createAdminFormGroup();

        const admin = service.getAdmin(formGroup) as any;

        expect(admin).toMatchObject({});
      });

      it('should return IAdmin', () => {
        const formGroup = service.createAdminFormGroup(sampleWithRequiredData);

        const admin = service.getAdmin(formGroup) as any;

        expect(admin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdmin should not enable id FormControl', () => {
        const formGroup = service.createAdminFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdmin should disable id FormControl', () => {
        const formGroup = service.createAdminFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
