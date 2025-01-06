import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVEducation, NewTechCVEducation } from '../tech-cv-education.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVEducation for edit and NewTechCVEducationFormGroupInput for create.
 */
type TechCVEducationFormGroupInput = ITechCVEducation | PartialWithRequiredKeyOf<NewTechCVEducation>;

type TechCVEducationFormDefaults = Pick<NewTechCVEducation, 'id'>;

type TechCVEducationFormGroupContent = {
  id: FormControl<ITechCVEducation['id'] | NewTechCVEducation['id']>;
  education: FormControl<ITechCVEducation['education']>;
  technicalCV: FormControl<ITechCVEducation['technicalCV']>;
};

export type TechCVEducationFormGroup = FormGroup<TechCVEducationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVEducationFormService {
  createTechCVEducationFormGroup(techCVEducation: TechCVEducationFormGroupInput = { id: null }): TechCVEducationFormGroup {
    const techCVEducationRawValue = {
      ...this.getFormDefaults(),
      ...techCVEducation,
    };
    return new FormGroup<TechCVEducationFormGroupContent>({
      id: new FormControl(
        { value: techCVEducationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      education: new FormControl(techCVEducationRawValue.education, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVEducationRawValue.technicalCV),
    });
  }

  getTechCVEducation(form: TechCVEducationFormGroup): ITechCVEducation | NewTechCVEducation {
    return form.getRawValue() as ITechCVEducation | NewTechCVEducation;
  }

  resetForm(form: TechCVEducationFormGroup, techCVEducation: TechCVEducationFormGroupInput): void {
    const techCVEducationRawValue = { ...this.getFormDefaults(), ...techCVEducation };
    form.reset(
      {
        ...techCVEducationRawValue,
        id: { value: techCVEducationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVEducationFormDefaults {
    return {
      id: null,
    };
  }
}
