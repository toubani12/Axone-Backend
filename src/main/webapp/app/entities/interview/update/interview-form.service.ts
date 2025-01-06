import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInterview, NewInterview } from '../interview.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInterview for edit and NewInterviewFormGroupInput for create.
 */
type InterviewFormGroupInput = IInterview | PartialWithRequiredKeyOf<NewInterview>;

type InterviewFormDefaults = Pick<NewInterview, 'id' | 'interviewResult'>;

type InterviewFormGroupContent = {
  id: FormControl<IInterview['id'] | NewInterview['id']>;
  label: FormControl<IInterview['label']>;
  entryLink: FormControl<IInterview['entryLink']>;
  invitationLink: FormControl<IInterview['invitationLink']>;
  interviewResult: FormControl<IInterview['interviewResult']>;
  rate: FormControl<IInterview['rate']>;
  startedAt: FormControl<IInterview['startedAt']>;
  endedAt: FormControl<IInterview['endedAt']>;
  comments: FormControl<IInterview['comments']>;
  attendee: FormControl<IInterview['attendee']>;
  application: FormControl<IInterview['application']>;
};

export type InterviewFormGroup = FormGroup<InterviewFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InterviewFormService {
  createInterviewFormGroup(interview: InterviewFormGroupInput = { id: null }): InterviewFormGroup {
    const interviewRawValue = {
      ...this.getFormDefaults(),
      ...interview,
    };
    return new FormGroup<InterviewFormGroupContent>({
      id: new FormControl(
        { value: interviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(interviewRawValue.label, {
        validators: [Validators.required],
      }),
      entryLink: new FormControl(interviewRawValue.entryLink, {
        validators: [Validators.required],
      }),
      invitationLink: new FormControl(interviewRawValue.invitationLink, {
        validators: [Validators.required],
      }),
      interviewResult: new FormControl(interviewRawValue.interviewResult),
      rate: new FormControl(interviewRawValue.rate),
      startedAt: new FormControl(interviewRawValue.startedAt),
      endedAt: new FormControl(interviewRawValue.endedAt),
      comments: new FormControl(interviewRawValue.comments),
      attendee: new FormControl(interviewRawValue.attendee),
      application: new FormControl(interviewRawValue.application, {
        validators: [Validators.required],
      }),
    });
  }

  getInterview(form: InterviewFormGroup): IInterview | NewInterview {
    return form.getRawValue() as IInterview | NewInterview;
  }

  resetForm(form: InterviewFormGroup, interview: InterviewFormGroupInput): void {
    const interviewRawValue = { ...this.getFormDefaults(), ...interview };
    form.reset(
      {
        ...interviewRawValue,
        id: { value: interviewRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InterviewFormDefaults {
    return {
      id: null,
      interviewResult: false,
    };
  }
}
