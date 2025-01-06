import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppAccount, NewAppAccount } from '../app-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppAccount for edit and NewAppAccountFormGroupInput for create.
 */
type AppAccountFormGroupInput = IAppAccount | PartialWithRequiredKeyOf<NewAppAccount>;

type AppAccountFormDefaults = Pick<NewAppAccount, 'id' | 'types' | 'providers'>;

type AppAccountFormGroupContent = {
  id: FormControl<IAppAccount['id'] | NewAppAccount['id']>;
  accountNumber: FormControl<IAppAccount['accountNumber']>;
  cardNumber: FormControl<IAppAccount['cardNumber']>;
  endDate: FormControl<IAppAccount['endDate']>;
  cvv: FormControl<IAppAccount['cvv']>;
  relatedUser: FormControl<IAppAccount['relatedUser']>;
  types: FormControl<IAppAccount['types']>;
  providers: FormControl<IAppAccount['providers']>;
  ifEmployer: FormControl<IAppAccount['ifEmployer']>;
};

export type AppAccountFormGroup = FormGroup<AppAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppAccountFormService {
  createAppAccountFormGroup(appAccount: AppAccountFormGroupInput = { id: null }): AppAccountFormGroup {
    const appAccountRawValue = {
      ...this.getFormDefaults(),
      ...appAccount,
    };
    return new FormGroup<AppAccountFormGroupContent>({
      id: new FormControl(
        { value: appAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accountNumber: new FormControl(appAccountRawValue.accountNumber, {
        validators: [Validators.required],
      }),
      cardNumber: new FormControl(appAccountRawValue.cardNumber),
      endDate: new FormControl(appAccountRawValue.endDate),
      cvv: new FormControl(appAccountRawValue.cvv),
      relatedUser: new FormControl(appAccountRawValue.relatedUser, {
        validators: [Validators.required],
      }),
      types: new FormControl(appAccountRawValue.types ?? []),
      providers: new FormControl(appAccountRawValue.providers ?? []),
      ifEmployer: new FormControl(appAccountRawValue.ifEmployer),
    });
  }

  getAppAccount(form: AppAccountFormGroup): IAppAccount | NewAppAccount {
    return form.getRawValue() as IAppAccount | NewAppAccount;
  }

  resetForm(form: AppAccountFormGroup, appAccount: AppAccountFormGroupInput): void {
    const appAccountRawValue = { ...this.getFormDefaults(), ...appAccount };
    form.reset(
      {
        ...appAccountRawValue,
        id: { value: appAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppAccountFormDefaults {
    return {
      id: null,
      types: [],
      providers: [],
    };
  }
}
