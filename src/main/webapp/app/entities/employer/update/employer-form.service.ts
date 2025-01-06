import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployer, NewEmployer } from '../employer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployer for edit and NewEmployerFormGroupInput for create.
 */
type EmployerFormGroupInput = IEmployer | PartialWithRequiredKeyOf<NewEmployer>;

type EmployerFormDefaults = Pick<NewEmployer, 'id'>;

type EmployerFormGroupContent = {
  id: FormControl<IEmployer['id'] | NewEmployer['id']>;
  firstName: FormControl<IEmployer['firstName']>;
  lastName: FormControl<IEmployer['lastName']>;
  profileImage: FormControl<IEmployer['profileImage']>;
  profileImageContentType: FormControl<IEmployer['profileImageContentType']>;
  address: FormControl<IEmployer['address']>;
  role: FormControl<IEmployer['role']>;
  status: FormControl<IEmployer['status']>;
  name: FormControl<IEmployer['name']>;
  label: FormControl<IEmployer['label']>;
  relatedUser: FormControl<IEmployer['relatedUser']>;
  wallet: FormControl<IEmployer['wallet']>;
};

export type EmployerFormGroup = FormGroup<EmployerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployerFormService {
  createEmployerFormGroup(employer: EmployerFormGroupInput = { id: null }): EmployerFormGroup {
    const employerRawValue = {
      ...this.getFormDefaults(),
      ...employer,
    };
    return new FormGroup<EmployerFormGroupContent>({
      id: new FormControl(
        { value: employerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(employerRawValue.firstName),
      lastName: new FormControl(employerRawValue.lastName),
      profileImage: new FormControl(employerRawValue.profileImage),
      profileImageContentType: new FormControl(employerRawValue.profileImageContentType),
      address: new FormControl(employerRawValue.address),
      role: new FormControl(employerRawValue.role, {
        validators: [Validators.required],
      }),
      status: new FormControl(employerRawValue.status, {
        validators: [Validators.required],
      }),
      name: new FormControl(employerRawValue.name),
      label: new FormControl(employerRawValue.label, {
        validators: [Validators.required],
      }),
      relatedUser: new FormControl(employerRawValue.relatedUser, {
        validators: [Validators.required],
      }),
      wallet: new FormControl(employerRawValue.wallet, {
        validators: [Validators.required],
      }),
    });
  }

  getEmployer(form: EmployerFormGroup): IEmployer | NewEmployer {
    return form.getRawValue() as IEmployer | NewEmployer;
  }

  resetForm(form: EmployerFormGroup, employer: EmployerFormGroupInput): void {
    const employerRawValue = { ...this.getFormDefaults(), ...employer };
    form.reset(
      {
        ...employerRawValue,
        id: { value: employerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployerFormDefaults {
    return {
      id: null,
    };
  }
}
