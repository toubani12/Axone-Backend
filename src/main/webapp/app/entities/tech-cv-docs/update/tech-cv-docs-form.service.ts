import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITechCVDocs, NewTechCVDocs } from '../tech-cv-docs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechCVDocs for edit and NewTechCVDocsFormGroupInput for create.
 */
type TechCVDocsFormGroupInput = ITechCVDocs | PartialWithRequiredKeyOf<NewTechCVDocs>;

type TechCVDocsFormDefaults = Pick<NewTechCVDocs, 'id'>;

type TechCVDocsFormGroupContent = {
  id: FormControl<ITechCVDocs['id'] | NewTechCVDocs['id']>;
  attachedDoc: FormControl<ITechCVDocs['attachedDoc']>;
  attachedDocContentType: FormControl<ITechCVDocs['attachedDocContentType']>;
  technicalCV: FormControl<ITechCVDocs['technicalCV']>;
};

export type TechCVDocsFormGroup = FormGroup<TechCVDocsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechCVDocsFormService {
  createTechCVDocsFormGroup(techCVDocs: TechCVDocsFormGroupInput = { id: null }): TechCVDocsFormGroup {
    const techCVDocsRawValue = {
      ...this.getFormDefaults(),
      ...techCVDocs,
    };
    return new FormGroup<TechCVDocsFormGroupContent>({
      id: new FormControl(
        { value: techCVDocsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      attachedDoc: new FormControl(techCVDocsRawValue.attachedDoc, {
        validators: [Validators.required],
      }),
      attachedDocContentType: new FormControl(techCVDocsRawValue.attachedDocContentType),
      technicalCV: new FormControl(techCVDocsRawValue.technicalCV),
    });
  }

  getTechCVDocs(form: TechCVDocsFormGroup): ITechCVDocs | NewTechCVDocs {
    return form.getRawValue() as ITechCVDocs | NewTechCVDocs;
  }

  resetForm(form: TechCVDocsFormGroup, techCVDocs: TechCVDocsFormGroupInput): void {
    const techCVDocsRawValue = { ...this.getFormDefaults(), ...techCVDocs };
    form.reset(
      {
        ...techCVDocsRawValue,
        id: { value: techCVDocsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechCVDocsFormDefaults {
    return {
      id: null,
    };
  }
}
