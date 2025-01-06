import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppTestType, NewAppTestType } from '../app-test-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppTestType for edit and NewAppTestTypeFormGroupInput for create.
 */
type AppTestTypeFormGroupInput = IAppTestType | PartialWithRequiredKeyOf<NewAppTestType>;

type AppTestTypeFormDefaults = Pick<NewAppTestType, 'id' | 'appTests'>;

type AppTestTypeFormGroupContent = {
  id: FormControl<IAppTestType['id'] | NewAppTestType['id']>;
  type: FormControl<IAppTestType['type']>;
  appTests: FormControl<IAppTestType['appTests']>;
};

export type AppTestTypeFormGroup = FormGroup<AppTestTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppTestTypeFormService {
  createAppTestTypeFormGroup(appTestType: AppTestTypeFormGroupInput = { id: null }): AppTestTypeFormGroup {
    const appTestTypeRawValue = {
      ...this.getFormDefaults(),
      ...appTestType,
    };
    return new FormGroup<AppTestTypeFormGroupContent>({
      id: new FormControl(
        { value: appTestTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(appTestTypeRawValue.type, {
        validators: [Validators.required],
      }),
      appTests: new FormControl(appTestTypeRawValue.appTests ?? []),
    });
  }

  getAppTestType(form: AppTestTypeFormGroup): IAppTestType | NewAppTestType {
    return form.getRawValue() as IAppTestType | NewAppTestType;
  }

  resetForm(form: AppTestTypeFormGroup, appTestType: AppTestTypeFormGroupInput): void {
    const appTestTypeRawValue = { ...this.getFormDefaults(), ...appTestType };
    form.reset(
      {
        ...appTestTypeRawValue,
        id: { value: appTestTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppTestTypeFormDefaults {
    return {
      id: null,
      appTests: [],
    };
  }
}
