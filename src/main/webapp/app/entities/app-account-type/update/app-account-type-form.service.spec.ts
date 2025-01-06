import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../app-account-type.test-samples';

import { AppAccountTypeFormService } from './app-account-type-form.service';

describe('AppAccountType Form Service', () => {
  let service: AppAccountTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppAccountTypeFormService);
  });

  describe('Service methods', () => {
    describe('createAppAccountTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppAccountTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            appAccounts: expect.any(Object),
          }),
        );
      });

      it('passing IAppAccountType should create a new form with FormGroup', () => {
        const formGroup = service.createAppAccountTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            appAccounts: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppAccountType', () => {
      it('should return NewAppAccountType for default AppAccountType initial value', () => {
        const formGroup = service.createAppAccountTypeFormGroup(sampleWithNewData);

        const appAccountType = service.getAppAccountType(formGroup) as any;

        expect(appAccountType).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppAccountType for empty AppAccountType initial value', () => {
        const formGroup = service.createAppAccountTypeFormGroup();

        const appAccountType = service.getAppAccountType(formGroup) as any;

        expect(appAccountType).toMatchObject({});
      });

      it('should return IAppAccountType', () => {
        const formGroup = service.createAppAccountTypeFormGroup(sampleWithRequiredData);

        const appAccountType = service.getAppAccountType(formGroup) as any;

        expect(appAccountType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppAccountType should not enable id FormControl', () => {
        const formGroup = service.createAppAccountTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppAccountType should disable id FormControl', () => {
        const formGroup = service.createAppAccountTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
