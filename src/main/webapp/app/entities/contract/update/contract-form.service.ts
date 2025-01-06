import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'directContract'>;

type ContractFormGroupContent = {
  id: FormControl<IContract['id'] | NewContract['id']>;
  label: FormControl<IContract['label']>;
  type: FormControl<IContract['type']>;
  status: FormControl<IContract['status']>;
  directContract: FormControl<IContract['directContract']>;
  paymentAmount: FormControl<IContract['paymentAmount']>;
  recruiterIncomeRate: FormControl<IContract['recruiterIncomeRate']>;
  candidateIncomeRate: FormControl<IContract['candidateIncomeRate']>;
  template: FormControl<IContract['template']>;
  candidate: FormControl<IContract['candidate']>;
  recruiter: FormControl<IContract['recruiter']>;
  employer: FormControl<IContract['employer']>;
  application: FormControl<IContract['application']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = {
      ...this.getFormDefaults(),
      ...contract,
    };
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(contractRawValue.label, {
        validators: [Validators.required],
      }),
      type: new FormControl(contractRawValue.type, {
        validators: [Validators.required],
      }),
      status: new FormControl(contractRawValue.status, {
        validators: [Validators.required],
      }),
      directContract: new FormControl(contractRawValue.directContract, {
        validators: [Validators.required],
      }),
      paymentAmount: new FormControl(contractRawValue.paymentAmount, {
        validators: [Validators.required],
      }),
      recruiterIncomeRate: new FormControl(contractRawValue.recruiterIncomeRate, {
        validators: [Validators.required],
      }),
      candidateIncomeRate: new FormControl(contractRawValue.candidateIncomeRate, {
        validators: [Validators.required],
      }),
      template: new FormControl(contractRawValue.template, {
        validators: [Validators.required],
      }),
      candidate: new FormControl(contractRawValue.candidate),
      recruiter: new FormControl(contractRawValue.recruiter, {
        validators: [Validators.required],
      }),
      employer: new FormControl(contractRawValue.employer, {
        validators: [Validators.required],
      }),
      application: new FormControl(contractRawValue.application, {
        validators: [Validators.required],
      }),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return form.getRawValue() as IContract | NewContract;
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = { ...this.getFormDefaults(), ...contract };
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    return {
      id: null,
      directContract: false,
    };
  }
}
