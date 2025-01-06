import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppAccountType, NewAppAccountType } from '../app-account-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppAccountType for edit and NewAppAccountTypeFormGroupInput for create.
 */
type AppAccountTypeFormGroupInput = IAppAccountType | PartialWithRequiredKeyOf<NewAppAccountType>;

type AppAccountTypeFormDefaults = Pick<NewAppAccountType, 'id' | 'appAccounts'>;

type AppAccountTypeFormGroupContent = {
  id: FormControl<IAppAccountType['id'] | NewAppAccountType['id']>;
  type: FormControl<IAppAccountType['type']>;
  appAccounts: FormControl<IAppAccountType['appAccounts']>;
};

export type AppAccountTypeFormGroup = FormGroup<AppAccountTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppAccountTypeFormService {
  createAppAccountTypeFormGroup(appAccountType: AppAccountTypeFormGroupInput = { id: null }): AppAccountTypeFormGroup {
    const appAccountTypeRawValue = {
      ...this.getFormDefaults(),
      ...appAccountType,
    };
    return new FormGroup<AppAccountTypeFormGroupContent>({
      id: new FormControl(
        { value: appAccountTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(appAccountTypeRawValue.type, {
        validators: [Validators.required],
      }),
      appAccounts: new FormControl(appAccountTypeRawValue.appAccounts ?? []),
    });
  }

  getAppAccountType(form: AppAccountTypeFormGroup): IAppAccountType | NewAppAccountType {
    return form.getRawValue() as IAppAccountType | NewAppAccountType;
  }

  resetForm(form: AppAccountTypeFormGroup, appAccountType: AppAccountTypeFormGroupInput): void {
    const appAccountTypeRawValue = { ...this.getFormDefaults(), ...appAccountType };
    form.reset(
      {
        ...appAccountTypeRawValue,
        id: { value: appAccountTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppAccountTypeFormDefaults {
    return {
      id: null,
      appAccounts: [],
    };
  }
}
