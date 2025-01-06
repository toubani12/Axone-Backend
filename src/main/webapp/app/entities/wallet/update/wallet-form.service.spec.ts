import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../wallet.test-samples';

import { WalletFormService } from './wallet-form.service';

describe('Wallet Form Service', () => {
  let service: WalletFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletFormService);
  });

  describe('Service methods', () => {
    describe('createWalletFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWalletFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            status: expect.any(Object),
            relatedToAccount: expect.any(Object),
          }),
        );
      });

      it('passing IWallet should create a new form with FormGroup', () => {
        const formGroup = service.createWalletFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            status: expect.any(Object),
            relatedToAccount: expect.any(Object),
          }),
        );
      });
    });

    describe('getWallet', () => {
      it('should return NewWallet for default Wallet initial value', () => {
        const formGroup = service.createWalletFormGroup(sampleWithNewData);

        const wallet = service.getWallet(formGroup) as any;

        expect(wallet).toMatchObject(sampleWithNewData);
      });

      it('should return NewWallet for empty Wallet initial value', () => {
        const formGroup = service.createWalletFormGroup();

        const wallet = service.getWallet(formGroup) as any;

        expect(wallet).toMatchObject({});
      });

      it('should return IWallet', () => {
        const formGroup = service.createWalletFormGroup(sampleWithRequiredData);

        const wallet = service.getWallet(formGroup) as any;

        expect(wallet).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWallet should not enable id FormControl', () => {
        const formGroup = service.createWalletFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWallet should disable id FormControl', () => {
        const formGroup = service.createWalletFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
