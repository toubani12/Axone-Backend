import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDomain, NewDomain } from '../domain.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDomain for edit and NewDomainFormGroupInput for create.
 */
type DomainFormGroupInput = IDomain | PartialWithRequiredKeyOf<NewDomain>;

type DomainFormDefaults = Pick<NewDomain, 'id' | 'recruiters' | 'candidates' | 'applications'>;

type DomainFormGroupContent = {
  id: FormControl<IDomain['id'] | NewDomain['id']>;
  name: FormControl<IDomain['name']>;
  recruiters: FormControl<IDomain['recruiters']>;
  candidates: FormControl<IDomain['candidates']>;
  applications: FormControl<IDomain['applications']>;
  employer: FormControl<IDomain['employer']>;
};

export type DomainFormGroup = FormGroup<DomainFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DomainFormService {
  createDomainFormGroup(domain: DomainFormGroupInput = { id: null }): DomainFormGroup {
    const domainRawValue = {
      ...this.getFormDefaults(),
      ...domain,
    };
    return new FormGroup<DomainFormGroupContent>({
      id: new FormControl(
        { value: domainRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(domainRawValue.name, {
        validators: [Validators.required],
      }),
      recruiters: new FormControl(domainRawValue.recruiters ?? []),
      candidates: new FormControl(domainRawValue.candidates ?? []),
      applications: new FormControl(domainRawValue.applications ?? []),
      employer: new FormControl(domainRawValue.employer),
    });
  }

  getDomain(form: DomainFormGroup): IDomain | NewDomain {
    return form.getRawValue() as IDomain | NewDomain;
  }

  resetForm(form: DomainFormGroup, domain: DomainFormGroupInput): void {
    const domainRawValue = { ...this.getFormDefaults(), ...domain };
    form.reset(
      {
        ...domainRawValue,
        id: { value: domainRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DomainFormDefaults {
    return {
      id: null,
      recruiters: [],
      candidates: [],
      applications: [],
    };
  }
}
