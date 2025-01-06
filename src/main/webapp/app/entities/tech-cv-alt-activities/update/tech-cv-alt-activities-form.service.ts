import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVAltActivities, NewTechCVAltActivities } from '../tech-cv-alt-activities.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVAltActivities for edit and NewTechCVAltActivitiesFormGroupInput for create.
 */
type TechCVAltActivitiesFormGroupInput = ITechCVAltActivities | PartialWithRequiredKeyOf<NewTechCVAltActivities>;

type TechCVAltActivitiesFormDefaults = Pick<NewTechCVAltActivities, 'id'>;

type TechCVAltActivitiesFormGroupContent = {
  id: FormControl<ITechCVAltActivities['id'] | NewTechCVAltActivities['id']>;
  activities: FormControl<ITechCVAltActivities['activities']>;
  technicalCV: FormControl<ITechCVAltActivities['technicalCV']>;
};

export type TechCVAltActivitiesFormGroup = FormGroup<TechCVAltActivitiesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVAltActivitiesFormService {
  createTechCVAltActivitiesFormGroup(techCVAltActivities: TechCVAltActivitiesFormGroupInput = { id: null }): TechCVAltActivitiesFormGroup {
    const techCVAltActivitiesRawValue = {
      ...this.getFormDefaults(),
      ...techCVAltActivities,
    };
    return new FormGroup<TechCVAltActivitiesFormGroupContent>({
      id: new FormControl(
        { value: techCVAltActivitiesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      activities: new FormControl(techCVAltActivitiesRawValue.activities, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVAltActivitiesRawValue.technicalCV),
    });
  }

  getTechCVAltActivities(form: TechCVAltActivitiesFormGroup): ITechCVAltActivities | NewTechCVAltActivities {
    return form.getRawValue() as ITechCVAltActivities | NewTechCVAltActivities;
  }

  resetForm(form: TechCVAltActivitiesFormGroup, techCVAltActivities: TechCVAltActivitiesFormGroupInput): void {
    const techCVAltActivitiesRawValue = { ...this.getFormDefaults(), ...techCVAltActivities };
    form.reset(
      {
        ...techCVAltActivitiesRawValue,
        id: { value: techCVAltActivitiesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVAltActivitiesFormDefaults {
    return {
      id: null,
    };
  }
}
