import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVHardSkills, NewTechCVHardSkills } from '../tech-cv-hard-skills.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVHardSkills for edit and NewTechCVHardSkillsFormGroupInput for create.
 */
type TechCVHardSkillsFormGroupInput = ITechCVHardSkills | PartialWithRequiredKeyOf<NewTechCVHardSkills>;

type TechCVHardSkillsFormDefaults = Pick<NewTechCVHardSkills, 'id'>;

type TechCVHardSkillsFormGroupContent = {
  id: FormControl<ITechCVHardSkills['id'] | NewTechCVHardSkills['id']>;
  skills: FormControl<ITechCVHardSkills['skills']>;
  technicalCV: FormControl<ITechCVHardSkills['technicalCV']>;
};

export type TechCVHardSkillsFormGroup = FormGroup<TechCVHardSkillsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVHardSkillsFormService {
  createTechCVHardSkillsFormGroup(techCVHardSkills: TechCVHardSkillsFormGroupInput = { id: null }): TechCVHardSkillsFormGroup {
    const techCVHardSkillsRawValue = {
      ...this.getFormDefaults(),
      ...techCVHardSkills,
    };
    return new FormGroup<TechCVHardSkillsFormGroupContent>({
      id: new FormControl(
        { value: techCVHardSkillsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      skills: new FormControl(techCVHardSkillsRawValue.skills, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVHardSkillsRawValue.technicalCV),
    });
  }

  getTechCVHardSkills(form: TechCVHardSkillsFormGroup): ITechCVHardSkills | NewTechCVHardSkills {
    return form.getRawValue() as ITechCVHardSkills | NewTechCVHardSkills;
  }

  resetForm(form: TechCVHardSkillsFormGroup, techCVHardSkills: TechCVHardSkillsFormGroupInput): void {
    const techCVHardSkillsRawValue = { ...this.getFormDefaults(), ...techCVHardSkills };
    form.reset(
      {
        ...techCVHardSkillsRawValue,
        id: { value: techCVHardSkillsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVHardSkillsFormDefaults {
    return {
      id: null,
    };
  }
}
