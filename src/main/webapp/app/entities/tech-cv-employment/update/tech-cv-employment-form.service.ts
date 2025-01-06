import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVEmployment, NewTechCVEmployment } from '../tech-cv-employment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVEmployment for edit and NewTechCVEmploymentFormGroupInput for create.
 */
type TechCVEmploymentFormGroupInput = ITechCVEmployment | PartialWithRequiredKeyOf<NewTechCVEmployment>;

type TechCVEmploymentFormDefaults = Pick<NewTechCVEmployment, 'id'>;

type TechCVEmploymentFormGroupContent = {
  id: FormControl<ITechCVEmployment['id'] | NewTechCVEmployment['id']>;
  employment: FormControl<ITechCVEmployment['employment']>;
  technicalCV: FormControl<ITechCVEmployment['technicalCV']>;
};

export type TechCVEmploymentFormGroup = FormGroup<TechCVEmploymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVEmploymentFormService {
  createTechCVEmploymentFormGroup(techCVEmployment: TechCVEmploymentFormGroupInput = { id: null }): TechCVEmploymentFormGroup {
    const techCVEmploymentRawValue = {
      ...this.getFormDefaults(),
      ...techCVEmployment,
    };
    return new FormGroup<TechCVEmploymentFormGroupContent>({
      id: new FormControl(
        { value: techCVEmploymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      employment: new FormControl(techCVEmploymentRawValue.employment, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVEmploymentRawValue.technicalCV),
    });
  }

  getTechCVEmployment(form: TechCVEmploymentFormGroup): ITechCVEmployment | NewTechCVEmployment {
    return form.getRawValue() as ITechCVEmployment | NewTechCVEmployment;
  }

  resetForm(form: TechCVEmploymentFormGroup, techCVEmployment: TechCVEmploymentFormGroupInput): void {
    const techCVEmploymentRawValue = { ...this.getFormDefaults(), ...techCVEmployment };
    form.reset(
      {
        ...techCVEmploymentRawValue,
        id: { value: techCVEmploymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVEmploymentFormDefaults {
    return {
      id: null,
    };
  }
}
