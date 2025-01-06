import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProvider, NewProvider } from '../provider.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProvider for edit and NewProviderFormGroupInput for create.
 */
type ProviderFormGroupInput = IProvider | PartialWithRequiredKeyOf<NewProvider>;

type ProviderFormDefaults = Pick<NewProvider, 'id' | 'appAccounts'>;

type ProviderFormGroupContent = {
  id: FormControl<IProvider['id'] | NewProvider['id']>;
  name: FormControl<IProvider['name']>;
  country: FormControl<IProvider['country']>;
  appAccounts: FormControl<IProvider['appAccounts']>;
};

export type ProviderFormGroup = FormGroup<ProviderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProviderFormService {
  createProviderFormGroup(provider: ProviderFormGroupInput = { id: null }): ProviderFormGroup {
    const providerRawValue = {
      ...this.getFormDefaults(),
      ...provider,
    };
    return new FormGroup<ProviderFormGroupContent>({
      id: new FormControl(
        { value: providerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(providerRawValue.name, {
        validators: [Validators.required],
      }),
      country: new FormControl(providerRawValue.country),
      appAccounts: new FormControl(providerRawValue.appAccounts ?? []),
    });
  }

  getProvider(form: ProviderFormGroup): IProvider | NewProvider {
    return form.getRawValue() as IProvider | NewProvider;
  }

  resetForm(form: ProviderFormGroup, provider: ProviderFormGroupInput): void {
    const providerRawValue = { ...this.getFormDefaults(), ...provider };
    form.reset(
      {
        ...providerRawValue,
        id: { value: providerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProviderFormDefaults {
    return {
      id: null,
      appAccounts: [],
    };
  }
}
