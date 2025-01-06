import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INDA, NewNDA } from '../nda.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INDA for edit and NewNDAFormGroupInput for create.
 */
type NDAFormGroupInput = INDA | PartialWithRequiredKeyOf<NewNDA>;

type NDAFormDefaults = Pick<NewNDA, 'id'>;

type NDAFormGroupContent = {
  id: FormControl<INDA['id'] | NewNDA['id']>;
  document: FormControl<INDA['document']>;
  documentContentType: FormControl<INDA['documentContentType']>;
  status: FormControl<INDA['status']>;
  period: FormControl<INDA['period']>;
  employer: FormControl<INDA['employer']>;
  mediator: FormControl<INDA['mediator']>;
  candidate: FormControl<INDA['candidate']>;
};

export type NDAFormGroup = FormGroup<NDAFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NDAFormService {
  createNDAFormGroup(nDA: NDAFormGroupInput = { id: null }): NDAFormGroup {
    const nDARawValue = {
      ...this.getFormDefaults(),
      ...nDA,
    };
    return new FormGroup<NDAFormGroupContent>({
      id: new FormControl(
        { value: nDARawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      document: new FormControl(nDARawValue.document, {
        validators: [Validators.required],
      }),
      documentContentType: new FormControl(nDARawValue.documentContentType),
      status: new FormControl(nDARawValue.status, {
        validators: [Validators.required],
      }),
      period: new FormControl(nDARawValue.period, {
        validators: [Validators.required],
      }),
      employer: new FormControl(nDARawValue.employer, {
        validators: [Validators.required],
      }),
      mediator: new FormControl(nDARawValue.mediator, {
        validators: [Validators.required],
      }),
      candidate: new FormControl(nDARawValue.candidate, {
        validators: [Validators.required],
      }),
    });
  }

  getNDA(form: NDAFormGroup): INDA | NewNDA {
    return form.getRawValue() as INDA | NewNDA;
  }

  resetForm(form: NDAFormGroup, nDA: NDAFormGroupInput): void {
    const nDARawValue = { ...this.getFormDefaults(), ...nDA };
    form.reset(
      {
        ...nDARawValue,
        id: { value: nDARawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NDAFormDefaults {
    return {
      id: null,
    };
  }
}
