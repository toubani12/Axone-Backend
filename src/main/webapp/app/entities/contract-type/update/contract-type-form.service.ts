import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContractType, NewContractType } from '../contract-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContractType for edit and NewContractTypeFormGroupInput for create.
 */
type ContractTypeFormGroupInput = IContractType | PartialWithRequiredKeyOf<NewContractType>;

type ContractTypeFormDefaults = Pick<NewContractType, 'id' | 'applications'>;

type ContractTypeFormGroupContent = {
  id: FormControl<IContractType['id'] | NewContractType['id']>;
  name: FormControl<IContractType['name']>;
  applications: FormControl<IContractType['applications']>;
};

export type ContractTypeFormGroup = FormGroup<ContractTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractTypeFormService {
  createContractTypeFormGroup(contractType: ContractTypeFormGroupInput = { id: null }): ContractTypeFormGroup {
    const contractTypeRawValue = {
      ...this.getFormDefaults(),
      ...contractType,
    };
    return new FormGroup<ContractTypeFormGroupContent>({
      id: new FormControl(
        { value: contractTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contractTypeRawValue.name, {
        validators: [Validators.required],
      }),
      applications: new FormControl(contractTypeRawValue.applications ?? []),
    });
  }

  getContractType(form: ContractTypeFormGroup): IContractType | NewContractType {
    return form.getRawValue() as IContractType | NewContractType;
  }

  resetForm(form: ContractTypeFormGroup, contractType: ContractTypeFormGroupInput): void {
    const contractTypeRawValue = { ...this.getFormDefaults(), ...contractType };
    form.reset(
      {
        ...contractTypeRawValue,
        id: { value: contractTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractTypeFormDefaults {
    return {
      id: null,
      applications: [],
    };
  }
}
