import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVAchievement, NewTechCVAchievement } from '../tech-cv-achievement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVAchievement for edit and NewTechCVAchievementFormGroupInput for create.
 */
type TechCVAchievementFormGroupInput = ITechCVAchievement | PartialWithRequiredKeyOf<NewTechCVAchievement>;

type TechCVAchievementFormDefaults = Pick<NewTechCVAchievement, 'id'>;

type TechCVAchievementFormGroupContent = {
  id: FormControl<ITechCVAchievement['id'] | NewTechCVAchievement['id']>;
  achievement: FormControl<ITechCVAchievement['achievement']>;
  technicalCV: FormControl<ITechCVAchievement['technicalCV']>;
};

export type TechCVAchievementFormGroup = FormGroup<TechCVAchievementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVAchievementFormService {
  createTechCVAchievementFormGroup(techCVAchievement: TechCVAchievementFormGroupInput = { id: null }): TechCVAchievementFormGroup {
    const techCVAchievementRawValue = {
      ...this.getFormDefaults(),
      ...techCVAchievement,
    };
    return new FormGroup<TechCVAchievementFormGroupContent>({
      id: new FormControl(
        { value: techCVAchievementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      achievement: new FormControl(techCVAchievementRawValue.achievement, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVAchievementRawValue.technicalCV),
    });
  }

  getTechCVAchievement(form: TechCVAchievementFormGroup): ITechCVAchievement | NewTechCVAchievement {
    return form.getRawValue() as ITechCVAchievement | NewTechCVAchievement;
  }

  resetForm(form: TechCVAchievementFormGroup, techCVAchievement: TechCVAchievementFormGroupInput): void {
    const techCVAchievementRawValue = { ...this.getFormDefaults(), ...techCVAchievement };
    form.reset(
      {
        ...techCVAchievementRawValue,
        id: { value: techCVAchievementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVAchievementFormDefaults {
    return {
      id: null,
    };
  }
}
