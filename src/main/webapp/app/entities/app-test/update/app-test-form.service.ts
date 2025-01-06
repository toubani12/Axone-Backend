import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppTest, NewAppTest } from '../app-test.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppTest for edit and NewAppTestFormGroupInput for create.
 */
type AppTestFormGroupInput = IAppTest | PartialWithRequiredKeyOf<NewAppTest>;

type AppTestFormDefaults = Pick<NewAppTest, 'id' | 'completed' | 'isCodeTest' | 'types'>;

type AppTestFormGroupContent = {
  id: FormControl<IAppTest['id'] | NewAppTest['id']>;
  name: FormControl<IAppTest['name']>;
  invitationLink: FormControl<IAppTest['invitationLink']>;
  totalScore: FormControl<IAppTest['totalScore']>;
  status: FormControl<IAppTest['status']>;
  completed: FormControl<IAppTest['completed']>;
  testID: FormControl<IAppTest['testID']>;
  algorithm: FormControl<IAppTest['algorithm']>;
  isCodeTest: FormControl<IAppTest['isCodeTest']>;
  duration: FormControl<IAppTest['duration']>;
  types: FormControl<IAppTest['types']>;
};

export type AppTestFormGroup = FormGroup<AppTestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppTestFormService {
  createAppTestFormGroup(appTest: AppTestFormGroupInput = { id: null }): AppTestFormGroup {
    const appTestRawValue = {
      ...this.getFormDefaults(),
      ...appTest,
    };
    return new FormGroup<AppTestFormGroupContent>({
      id: new FormControl(
        { value: appTestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(appTestRawValue.name, {
        validators: [Validators.required],
      }),
      invitationLink: new FormControl(appTestRawValue.invitationLink, {
        validators: [Validators.required],
      }),
      totalScore: new FormControl(appTestRawValue.totalScore),
      status: new FormControl(appTestRawValue.status),
      completed: new FormControl(appTestRawValue.completed),
      testID: new FormControl(appTestRawValue.testID),
      algorithm: new FormControl(appTestRawValue.algorithm),
      isCodeTest: new FormControl(appTestRawValue.isCodeTest),
      duration: new FormControl(appTestRawValue.duration),
      types: new FormControl(appTestRawValue.types ?? []),
    });
  }

  getAppTest(form: AppTestFormGroup): IAppTest | NewAppTest {
    return form.getRawValue() as IAppTest | NewAppTest;
  }

  resetForm(form: AppTestFormGroup, appTest: AppTestFormGroupInput): void {
    const appTestRawValue = { ...this.getFormDefaults(), ...appTest };
    form.reset(
      {
        ...appTestRawValue,
        id: { value: appTestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppTestFormDefaults {
    return {
      id: null,
      completed: false,
      isCodeTest: false,
      types: [],
    };
  }
}
