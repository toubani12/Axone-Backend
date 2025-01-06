import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../app-account.test-samples';

import { AppAccountFormService } from './app-account-form.service';

describe('AppAccount Form Service', () => {
  let service: AppAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppAccountFormService);
  });

  describe('Service methods', () => {
    describe('createAppAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNumber: expect.any(Object),
            cardNumber: expect.any(Object),
            endDate: expect.any(Object),
            cvv: expect.any(Object),
            relatedUser: expect.any(Object),
            types: expect.any(Object),
            providers: expect.any(Object),
            ifEmployer: expect.any(Object),
          }),
        );
      });

      it('passing IAppAccount should create a new form with FormGroup', () => {
        const formGroup = service.createAppAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNumber: expect.any(Object),
            cardNumber: expect.any(Object),
            endDate: expect.any(Object),
            cvv: expect.any(Object),
            relatedUser: expect.any(Object),
            types: expect.any(Object),
            providers: expect.any(Object),
            ifEmployer: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppAccount', () => {
      it('should return NewAppAccount for default AppAccount initial value', () => {
        const formGroup = service.createAppAccountFormGroup(sampleWithNewData);

        const appAccount = service.getAppAccount(formGroup) as any;

        expect(appAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppAccount for empty AppAccount initial value', () => {
        const formGroup = service.createAppAccountFormGroup();

        const appAccount = service.getAppAccount(formGroup) as any;

        expect(appAccount).toMatchObject({});
      });

      it('should return IAppAccount', () => {
        const formGroup = service.createAppAccountFormGroup(sampleWithRequiredData);

        const appAccount = service.getAppAccount(formGroup) as any;

        expect(appAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppAccount should not enable id FormControl', () => {
        const formGroup = service.createAppAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppAccount should disable id FormControl', () => {
        const formGroup = service.createAppAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
