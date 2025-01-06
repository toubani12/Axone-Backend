import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApplication, NewApplication } from '../application.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplication for edit and NewApplicationFormGroupInput for create.
 */
type ApplicationFormGroupInput = IApplication | PartialWithRequiredKeyOf<NewApplication>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApplication | NewApplication> = Omit<T, 'createdAt' | 'openedAt' | 'closedAt' | 'doneAt'> & {
  createdAt?: string | null;
  openedAt?: string | null;
  closedAt?: string | null;
  doneAt?: string | null;
};

type ApplicationFormRawValue = FormValueOf<IApplication>;

type NewApplicationFormRawValue = FormValueOf<NewApplication>;

type ApplicationFormDefaults = Pick<
  NewApplication,
  | 'id'
  | 'createdAt'
  | 'openedAt'
  | 'closedAt'
  | 'doneAt'
  | 'contractTypes'
  | 'contractTemplates'
  | 'criteria'
  | 'domains'
  | 'recruiters'
  | 'candidates'
>;

type ApplicationFormGroupContent = {
  id: FormControl<ApplicationFormRawValue['id'] | NewApplication['id']>;
  title: FormControl<ApplicationFormRawValue['title']>;
  description: FormControl<ApplicationFormRawValue['description']>;
  numberOfCandidates: FormControl<ApplicationFormRawValue['numberOfCandidates']>;
  paymentAmount: FormControl<ApplicationFormRawValue['paymentAmount']>;
  recruiterIncomeRate: FormControl<ApplicationFormRawValue['recruiterIncomeRate']>;
  candidateIncomeRate: FormControl<ApplicationFormRawValue['candidateIncomeRate']>;
  deadline: FormControl<ApplicationFormRawValue['deadline']>;
  status: FormControl<ApplicationFormRawValue['status']>;
  createdAt: FormControl<ApplicationFormRawValue['createdAt']>;
  openedAt: FormControl<ApplicationFormRawValue['openedAt']>;
  closedAt: FormControl<ApplicationFormRawValue['closedAt']>;
  doneAt: FormControl<ApplicationFormRawValue['doneAt']>;
  contractTypes: FormControl<ApplicationFormRawValue['contractTypes']>;
  contractTemplates: FormControl<ApplicationFormRawValue['contractTemplates']>;
  criteria: FormControl<ApplicationFormRawValue['criteria']>;
  domains: FormControl<ApplicationFormRawValue['domains']>;
  employer: FormControl<ApplicationFormRawValue['employer']>;
  recruiters: FormControl<ApplicationFormRawValue['recruiters']>;
  candidates: FormControl<ApplicationFormRawValue['candidates']>;
};

export type ApplicationFormGroup = FormGroup<ApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationFormService {
  createApplicationFormGroup(application: ApplicationFormGroupInput = { id: null }): ApplicationFormGroup {
    const applicationRawValue = this.convertApplicationToApplicationRawValue({
      ...this.getFormDefaults(),
      ...application,
    });
    return new FormGroup<ApplicationFormGroupContent>({
      id: new FormControl(
        { value: applicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(applicationRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(applicationRawValue.description, {
        validators: [Validators.required],
      }),
      numberOfCandidates: new FormControl(applicationRawValue.numberOfCandidates, {
        validators: [Validators.required],
      }),
      paymentAmount: new FormControl(applicationRawValue.paymentAmount, {
        validators: [Validators.required],
      }),
      recruiterIncomeRate: new FormControl(applicationRawValue.recruiterIncomeRate),
      candidateIncomeRate: new FormControl(applicationRawValue.candidateIncomeRate),
      deadline: new FormControl(applicationRawValue.deadline),
      status: new FormControl(applicationRawValue.status, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(applicationRawValue.createdAt),
      openedAt: new FormControl(applicationRawValue.openedAt),
      closedAt: new FormControl(applicationRawValue.closedAt),
      doneAt: new FormControl(applicationRawValue.doneAt),
      contractTypes: new FormControl(applicationRawValue.contractTypes ?? []),
      contractTemplates: new FormControl(applicationRawValue.contractTemplates ?? []),
      criteria: new FormControl(applicationRawValue.criteria ?? []),
      domains: new FormControl(applicationRawValue.domains ?? []),
      employer: new FormControl(applicationRawValue.employer, {
        validators: [Validators.required],
      }),
      recruiters: new FormControl(applicationRawValue.recruiters ?? []),
      candidates: new FormControl(applicationRawValue.candidates ?? []),
    });
  }

  getApplication(form: ApplicationFormGroup): IApplication | NewApplication {
    return this.convertApplicationRawValueToApplication(form.getRawValue() as ApplicationFormRawValue | NewApplicationFormRawValue);
  }

  resetForm(form: ApplicationFormGroup, application: ApplicationFormGroupInput): void {
    const applicationRawValue = this.convertApplicationToApplicationRawValue({ ...this.getFormDefaults(), ...application });
    form.reset(
      {
        ...applicationRawValue,
        id: { value: applicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApplicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      openedAt: currentTime,
      closedAt: currentTime,
      doneAt: currentTime,
      contractTypes: [],
      contractTemplates: [],
      criteria: [],
      domains: [],
      recruiters: [],
      candidates: [],
    };
  }

  private convertApplicationRawValueToApplication(
    rawApplication: ApplicationFormRawValue | NewApplicationFormRawValue,
  ): IApplication | NewApplication {
    return {
      ...rawApplication,
      createdAt: dayjs(rawApplication.createdAt, DATE_TIME_FORMAT),
      openedAt: dayjs(rawApplication.openedAt, DATE_TIME_FORMAT),
      closedAt: dayjs(rawApplication.closedAt, DATE_TIME_FORMAT),
      doneAt: dayjs(rawApplication.doneAt, DATE_TIME_FORMAT),
    };
  }

  private convertApplicationToApplicationRawValue(
    application: IApplication | (Partial<NewApplication> & ApplicationFormDefaults),
  ): ApplicationFormRawValue | PartialWithRequiredKeyOf<NewApplicationFormRawValue> {
    return {
      ...application,
      createdAt: application.createdAt ? application.createdAt.format(DATE_TIME_FORMAT) : undefined,
      openedAt: application.openedAt ? application.openedAt.format(DATE_TIME_FORMAT) : undefined,
      closedAt: application.closedAt ? application.closedAt.format(DATE_TIME_FORMAT) : undefined,
      doneAt: application.doneAt ? application.doneAt.format(DATE_TIME_FORMAT) : undefined,
      contractTypes: application.contractTypes ?? [],
      contractTemplates: application.contractTemplates ?? [],
      criteria: application.criteria ?? [],
      domains: application.domains ?? [],
      recruiters: application.recruiters ?? [],
      candidates: application.candidates ?? [],
    };
  }
}
