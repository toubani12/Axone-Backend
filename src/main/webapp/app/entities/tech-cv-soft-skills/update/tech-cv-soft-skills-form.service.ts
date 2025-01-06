import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVSoftSkills, NewTechCVSoftSkills } from '../tech-cv-soft-skills.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVSoftSkills for edit and NewTechCVSoftSkillsFormGroupInput for create.
 */
type TechCVSoftSkillsFormGroupInput = ITechCVSoftSkills | PartialWithRequiredKeyOf<NewTechCVSoftSkills>;

type TechCVSoftSkillsFormDefaults = Pick<NewTechCVSoftSkills, 'id'>;

type TechCVSoftSkillsFormGroupContent = {
  id: FormControl<ITechCVSoftSkills['id'] | NewTechCVSoftSkills['id']>;
  skills: FormControl<ITechCVSoftSkills['skills']>;
  technicalCV: FormControl<ITechCVSoftSkills['technicalCV']>;
};

export type TechCVSoftSkillsFormGroup = FormGroup<TechCVSoftSkillsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVSoftSkillsFormService {
  createTechCVSoftSkillsFormGroup(techCVSoftSkills: TechCVSoftSkillsFormGroupInput = { id: null }): TechCVSoftSkillsFormGroup {
    const techCVSoftSkillsRawValue = {
      ...this.getFormDefaults(),
      ...techCVSoftSkills,
    };
    return new FormGroup<TechCVSoftSkillsFormGroupContent>({
      id: new FormControl(
        { value: techCVSoftSkillsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      skills: new FormControl(techCVSoftSkillsRawValue.skills, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVSoftSkillsRawValue.technicalCV),
    });
  }

  getTechCVSoftSkills(form: TechCVSoftSkillsFormGroup): ITechCVSoftSkills | NewTechCVSoftSkills {
    return form.getRawValue() as ITechCVSoftSkills | NewTechCVSoftSkills;
  }

  resetForm(form: TechCVSoftSkillsFormGroup, techCVSoftSkills: TechCVSoftSkillsFormGroupInput): void {
    const techCVSoftSkillsRawValue = { ...this.getFormDefaults(), ...techCVSoftSkills };
    form.reset(
      {
        ...techCVSoftSkillsRawValue,
        id: { value: techCVSoftSkillsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVSoftSkillsFormDefaults {
    return {
      id: null,
    };
  }
}
