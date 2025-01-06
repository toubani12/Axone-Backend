import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVProject, NewTechCVProject } from '../tech-cv-project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVProject for edit and NewTechCVProjectFormGroupInput for create.
 */
type TechCVProjectFormGroupInput = ITechCVProject | PartialWithRequiredKeyOf<NewTechCVProject>;

type TechCVProjectFormDefaults = Pick<NewTechCVProject, 'id'>;

type TechCVProjectFormGroupContent = {
  id: FormControl<ITechCVProject['id'] | NewTechCVProject['id']>;
  project: FormControl<ITechCVProject['project']>;
  technicalCV: FormControl<ITechCVProject['technicalCV']>;
};

export type TechCVProjectFormGroup = FormGroup<TechCVProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVProjectFormService {
  createTechCVProjectFormGroup(techCVProject: TechCVProjectFormGroupInput = { id: null }): TechCVProjectFormGroup {
    const techCVProjectRawValue = {
      ...this.getFormDefaults(),
      ...techCVProject,
    };
    return new FormGroup<TechCVProjectFormGroupContent>({
      id: new FormControl(
        { value: techCVProjectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      project: new FormControl(techCVProjectRawValue.project, {
        validators: [Validators.required],
      }),
      technicalCV: new FormControl(techCVProjectRawValue.technicalCV),
    });
  }

  getTechCVProject(form: TechCVProjectFormGroup): ITechCVProject | NewTechCVProject {
    return form.getRawValue() as ITechCVProject | NewTechCVProject;
  }

  resetForm(form: TechCVProjectFormGroup, techCVProject: TechCVProjectFormGroupInput): void {
    const techCVProjectRawValue = { ...this.getFormDefaults(), ...techCVProject };
    form.reset(
      {
        ...techCVProjectRawValue,
        id: { value: techCVProjectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVProjectFormDefaults {
    return {
      id: null,
    };
  }
}
