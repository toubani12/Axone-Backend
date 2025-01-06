import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechnicalCV, NewTechnicalCV } from '../technical-cv.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechnicalCV for edit and NewTechnicalCVFormGroupInput for create.
 */
type TechnicalCVFormGroupInput = ITechnicalCV | PartialWithRequiredKeyOf<NewTechnicalCV>;

type TechnicalCVFormDefaults = Pick<NewTechnicalCV, 'id'>;

type TechnicalCVFormGroupContent = {
  id: FormControl<ITechnicalCV['id'] | NewTechnicalCV['id']>;
  name: FormControl<ITechnicalCV['name']>;
  level: FormControl<ITechnicalCV['level']>;
  profileDescription: FormControl<ITechnicalCV['profileDescription']>;
};

export type TechnicalCVFormGroup = FormGroup<TechnicalCVFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechnicalCVFormService {
  createTechnicalCVFormGroup(technicalCV: TechnicalCVFormGroupInput = { id: null }): TechnicalCVFormGroup {
    const technicalCVRawValue = {
      ...this.getFormDefaults(),
      ...technicalCV,
    };
    return new FormGroup<TechnicalCVFormGroupContent>({
      id: new FormControl(
        { value: technicalCVRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(technicalCVRawValue.name, {
        validators: [Validators.required],
      }),
      level: new FormControl(technicalCVRawValue.level, {
        validators: [Validators.required],
      }),
      profileDescription: new FormControl(technicalCVRawValue.profileDescription),
    });
  }

  getTechnicalCV(form: TechnicalCVFormGroup): ITechnicalCV | NewTechnicalCV {
    return form.getRawValue() as ITechnicalCV | NewTechnicalCV;
  }

  resetForm(form: TechnicalCVFormGroup, technicalCV: TechnicalCVFormGroupInput): void {
    const technicalCVRawValue = { ...this.getFormDefaults(), ...technicalCV };
    form.reset(
      {
        ...technicalCVRawValue,
        id: { value: technicalCVRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechnicalCVFormDefaults {
    return {
      id: null,
    };
  }
}
