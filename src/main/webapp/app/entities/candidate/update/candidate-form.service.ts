import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICandidate, NewCandidate } from '../candidate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICandidate for edit and NewCandidateFormGroupInput for create.
 */
type CandidateFormGroupInput = ICandidate | PartialWithRequiredKeyOf<NewCandidate>;

type CandidateFormDefaults = Pick<NewCandidate, 'id' | 'hasContract' | 'hired' | 'domains' | 'applications'>;

type CandidateFormGroupContent = {
  id: FormControl<ICandidate['id'] | NewCandidate['id']>;
  firstName: FormControl<ICandidate['firstName']>;
  lastName: FormControl<ICandidate['lastName']>;
  linkedinUrl: FormControl<ICandidate['linkedinUrl']>;
  fullName: FormControl<ICandidate['fullName']>;
  yearsOfExperience: FormControl<ICandidate['yearsOfExperience']>;
  currentSalary: FormControl<ICandidate['currentSalary']>;
  desiredSalary: FormControl<ICandidate['desiredSalary']>;
  hasContract: FormControl<ICandidate['hasContract']>;
  hired: FormControl<ICandidate['hired']>;
  rate: FormControl<ICandidate['rate']>;
  techCV: FormControl<ICandidate['techCV']>;
  domains: FormControl<ICandidate['domains']>;
  applications: FormControl<ICandidate['applications']>;
};

export type CandidateFormGroup = FormGroup<CandidateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CandidateFormService {
  createCandidateFormGroup(candidate: CandidateFormGroupInput = { id: null }): CandidateFormGroup {
    const candidateRawValue = {
      ...this.getFormDefaults(),
      ...candidate,
    };
    return new FormGroup<CandidateFormGroupContent>({
      id: new FormControl(
        { value: candidateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(candidateRawValue.firstName),
      lastName: new FormControl(candidateRawValue.lastName),
      linkedinUrl: new FormControl(candidateRawValue.linkedinUrl),
      fullName: new FormControl(candidateRawValue.fullName, {
        validators: [Validators.required],
      }),
      yearsOfExperience: new FormControl(candidateRawValue.yearsOfExperience, {
        validators: [Validators.required],
      }),
      currentSalary: new FormControl(candidateRawValue.currentSalary),
      desiredSalary: new FormControl(candidateRawValue.desiredSalary),
      hasContract: new FormControl(candidateRawValue.hasContract, {
        validators: [Validators.required],
      }),
      hired: new FormControl(candidateRawValue.hired, {
        validators: [Validators.required],
      }),
      rate: new FormControl(candidateRawValue.rate),
      techCV: new FormControl(candidateRawValue.techCV, {
        validators: [Validators.required],
      }),
      domains: new FormControl(candidateRawValue.domains ?? []),
      applications: new FormControl(candidateRawValue.applications ?? []),
    });
  }

  getCandidate(form: CandidateFormGroup): ICandidate | NewCandidate {
    return form.getRawValue() as ICandidate | NewCandidate;
  }

  resetForm(form: CandidateFormGroup, candidate: CandidateFormGroupInput): void {
    const candidateRawValue = { ...this.getFormDefaults(), ...candidate };
    form.reset(
      {
        ...candidateRawValue,
        id: { value: candidateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CandidateFormDefaults {
    return {
      id: null,
      hasContract: false,
      hired: false,
      domains: [],
      applications: [],
    };
  }
}
