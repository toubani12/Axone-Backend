import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITemplate, NewTemplate } from '../template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITemplate for edit and NewTemplateFormGroupInput for create.
 */
type TemplateFormGroupInput = ITemplate | PartialWithRequiredKeyOf<NewTemplate>;

type TemplateFormDefaults = Pick<NewTemplate, 'id' | 'standard' | 'applications'>;

type TemplateFormGroupContent = {
  id: FormControl<ITemplate['id'] | NewTemplate['id']>;
  label: FormControl<ITemplate['label']>;
  type: FormControl<ITemplate['type']>;
  description: FormControl<ITemplate['description']>;
  standard: FormControl<ITemplate['standard']>;
  docLink: FormControl<ITemplate['docLink']>;
  docLinkContentType: FormControl<ITemplate['docLinkContentType']>;
  owner: FormControl<ITemplate['owner']>;
  applications: FormControl<ITemplate['applications']>;
};

export type TemplateFormGroup = FormGroup<TemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TemplateFormService {
  createTemplateFormGroup(template: TemplateFormGroupInput = { id: null }): TemplateFormGroup {
    const templateRawValue = {
      ...this.getFormDefaults(),
      ...template,
    };
    return new FormGroup<TemplateFormGroupContent>({
      id: new FormControl(
        { value: templateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(templateRawValue.label, {
        validators: [Validators.required],
      }),
      type: new FormControl(templateRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(templateRawValue.description),
      standard: new FormControl(templateRawValue.standard, {
        validators: [Validators.required],
      }),
      docLink: new FormControl(templateRawValue.docLink, {
        validators: [Validators.required],
      }),
      docLinkContentType: new FormControl(templateRawValue.docLinkContentType),
      owner: new FormControl(templateRawValue.owner, {
        validators: [Validators.required],
      }),
      applications: new FormControl(templateRawValue.applications ?? []),
    });
  }

  getTemplate(form: TemplateFormGroup): ITemplate | NewTemplate {
    return form.getRawValue() as ITemplate | NewTemplate;
  }

  resetForm(form: TemplateFormGroup, template: TemplateFormGroupInput): void {
    const templateRawValue = { ...this.getFormDefaults(), ...template };
    form.reset(
      {
        ...templateRawValue,
        id: { value: templateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TemplateFormDefaults {
    return {
      id: null,
      standard: false,
      applications: [],
    };
  }
}
