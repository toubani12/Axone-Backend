import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../provider.test-samples';

import { ProviderFormService } from './provider-form.service';

describe('Provider Form Service', () => {
  let service: ProviderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProviderFormService);
  });

  describe('Service methods', () => {
    describe('createProviderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProviderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            country: expect.any(Object),
            appAccounts: expect.any(Object),
          }),
        );
      });

      it('passing IProvider should create a new form with FormGroup', () => {
        const formGroup = service.createProviderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            country: expect.any(Object),
            appAccounts: expect.any(Object),
          }),
        );
      });
    });

    describe('getProvider', () => {
      it('should return NewProvider for default Provider initial value', () => {
        const formGroup = service.createProviderFormGroup(sampleWithNewData);

        const provider = service.getProvider(formGroup) as any;

        expect(provider).toMatchObject(sampleWithNewData);
      });

      it('should return NewProvider for empty Provider initial value', () => {
        const formGroup = service.createProviderFormGroup();

        const provider = service.getProvider(formGroup) as any;

        expect(provider).toMatchObject({});
      });

      it('should return IProvider', () => {
        const formGroup = service.createProviderFormGroup(sampleWithRequiredData);

        const provider = service.getProvider(formGroup) as any;

        expect(provider).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProvider should not enable id FormControl', () => {
        const formGroup = service.createProviderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProvider should disable id FormControl', () => {
        const formGroup = service.createProviderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
