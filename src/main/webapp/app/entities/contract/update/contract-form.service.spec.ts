import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contract.test-samples';

import { ContractFormService } from './contract-form.service';

describe('Contract Form Service', () => {
  let service: ContractFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractFormService);
  });

  describe('Service methods', () => {
    describe('createContractFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            directContract: expect.any(Object),
            paymentAmount: expect.any(Object),
            recruiterIncomeRate: expect.any(Object),
            candidateIncomeRate: expect.any(Object),
            template: expect.any(Object),
            candidate: expect.any(Object),
            recruiter: expect.any(Object),
            employer: expect.any(Object),
            application: expect.any(Object),
          }),
        );
      });

      it('passing IContract should create a new form with FormGroup', () => {
        const formGroup = service.createContractFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            directContract: expect.any(Object),
            paymentAmount: expect.any(Object),
            recruiterIncomeRate: expect.any(Object),
            candidateIncomeRate: expect.any(Object),
            template: expect.any(Object),
            candidate: expect.any(Object),
            recruiter: expect.any(Object),
            employer: expect.any(Object),
            application: expect.any(Object),
          }),
        );
      });
    });

    describe('getContract', () => {
      it('should return NewContract for default Contract initial value', () => {
        const formGroup = service.createContractFormGroup(sampleWithNewData);

        const contract = service.getContract(formGroup) as any;

        expect(contract).toMatchObject(sampleWithNewData);
      });

      it('should return NewContract for empty Contract initial value', () => {
        const formGroup = service.createContractFormGroup();

        const contract = service.getContract(formGroup) as any;

        expect(contract).toMatchObject({});
      });

      it('should return IContract', () => {
        const formGroup = service.createContractFormGroup(sampleWithRequiredData);

        const contract = service.getContract(formGroup) as any;

        expect(contract).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContract should not enable id FormControl', () => {
        const formGroup = service.createContractFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContract should disable id FormControl', () => {
        const formGroup = service.createContractFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
