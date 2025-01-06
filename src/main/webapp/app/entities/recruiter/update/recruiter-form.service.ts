import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRecruiter, NewRecruiter } from '../recruiter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecruiter for edit and NewRecruiterFormGroupInput for create.
 */
type RecruiterFormGroupInput = IRecruiter | PartialWithRequiredKeyOf<NewRecruiter>;

type RecruiterFormDefaults = Pick<NewRecruiter, 'id' | 'approvedExperience' | 'applications' | 'operationalDomains'>;

type RecruiterFormGroupContent = {
  id: FormControl<IRecruiter['id'] | NewRecruiter['id']>;
  firstName: FormControl<IRecruiter['firstName']>;
  lastName: FormControl<IRecruiter['lastName']>;
  profileImage: FormControl<IRecruiter['profileImage']>;
  profileImageContentType: FormControl<IRecruiter['profileImageContentType']>;
  address: FormControl<IRecruiter['address']>;
  role: FormControl<IRecruiter['role']>;
  status: FormControl<IRecruiter['status']>;
  name: FormControl<IRecruiter['name']>;
  label: FormControl<IRecruiter['label']>;
  linkedinUrl: FormControl<IRecruiter['linkedinUrl']>;
  approvedExperience: FormControl<IRecruiter['approvedExperience']>;
  score: FormControl<IRecruiter['score']>;
  relatedUser: FormControl<IRecruiter['relatedUser']>;
  wallet: FormControl<IRecruiter['wallet']>;
  applications: FormControl<IRecruiter['applications']>;
  operationalDomains: FormControl<IRecruiter['operationalDomains']>;
};

export type RecruiterFormGroup = FormGroup<RecruiterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecruiterFormService {
  createRecruiterFormGroup(recruiter: RecruiterFormGroupInput = { id: null }): RecruiterFormGroup {
    const recruiterRawValue = {
      ...this.getFormDefaults(),
      ...recruiter,
    };
    return new FormGroup<RecruiterFormGroupContent>({
      id: new FormControl(
        { value: recruiterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(recruiterRawValue.firstName),
      lastName: new FormControl(recruiterRawValue.lastName),
      profileImage: new FormControl(recruiterRawValue.profileImage),
      profileImageContentType: new FormControl(recruiterRawValue.profileImageContentType),
      address: new FormControl(recruiterRawValue.address),
      role: new FormControl(recruiterRawValue.role, {
        validators: [Validators.required],
      }),
      status: new FormControl(recruiterRawValue.status, {
        validators: [Validators.required],
      }),
      name: new FormControl(recruiterRawValue.name),
      label: new FormControl(recruiterRawValue.label, {
        validators: [Validators.required],
      }),
      linkedinUrl: new FormControl(recruiterRawValue.linkedinUrl, {
        validators: [Validators.required],
      }),
      approvedExperience: new FormControl(recruiterRawValue.approvedExperience),
      score: new FormControl(recruiterRawValue.score),
      relatedUser: new FormControl(recruiterRawValue.relatedUser, {
        validators: [Validators.required],
      }),
      wallet: new FormControl(recruiterRawValue.wallet, {
        validators: [Validators.required],
      }),
      applications: new FormControl(recruiterRawValue.applications ?? []),
      operationalDomains: new FormControl(recruiterRawValue.operationalDomains ?? []),
    });
  }

  getRecruiter(form: RecruiterFormGroup): IRecruiter | NewRecruiter {
    return form.getRawValue() as IRecruiter | NewRecruiter;
  }

  resetForm(form: RecruiterFormGroup, recruiter: RecruiterFormGroupInput): void {
    const recruiterRawValue = { ...this.getFormDefaults(), ...recruiter };
    form.reset(
      {
        ...recruiterRawValue,
        id: { value: recruiterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecruiterFormDefaults {
    return {
      id: null,
      approvedExperience: false,
      applications: [],
      operationalDomains: [],
    };
  }
}
