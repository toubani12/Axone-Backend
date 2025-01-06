import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contract-type.test-samples';

import { ContractTypeFormService } from './contract-type-form.service';

describe('ContractType Form Service', () => {
  let service: ContractTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractTypeFormService);
  });

  describe('Service methods', () => {
    describe('createContractTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });

      it('passing IContractType should create a new form with FormGroup', () => {
        const formGroup = service.createContractTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });
    });

    describe('getContractType', () => {
      it('should return NewContractType for default ContractType initial value', () => {
        const formGroup = service.createContractTypeFormGroup(sampleWithNewData);

        const contractType = service.getContractType(formGroup) as any;

        expect(contractType).toMatchObject(sampleWithNewData);
      });

      it('should return NewContractType for empty ContractType initial value', () => {
        const formGroup = service.createContractTypeFormGroup();

        const contractType = service.getContractType(formGroup) as any;

        expect(contractType).toMatchObject({});
      });

      it('should return IContractType', () => {
        const formGroup = service.createContractTypeFormGroup(sampleWithRequiredData);

        const contractType = service.getContractType(formGroup) as any;

        expect(contractType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContractType should not enable id FormControl', () => {
        const formGroup = service.createContractTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContractType should disable id FormControl', () => {
        const formGroup = service.createContractTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
