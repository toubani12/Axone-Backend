import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomQuestion, NewCustomQuestion } from '../custom-question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomQuestion for edit and NewCustomQuestionFormGroupInput for create.
 */
type CustomQuestionFormGroupInput = ICustomQuestion | PartialWithRequiredKeyOf<NewCustomQuestion>;

type CustomQuestionFormDefaults = Pick<NewCustomQuestion, 'id'>;

type CustomQuestionFormGroupContent = {
  id: FormControl<ICustomQuestion['id'] | NewCustomQuestion['id']>;
  question: FormControl<ICustomQuestion['question']>;
  answer: FormControl<ICustomQuestion['answer']>;
  correctAnswer: FormControl<ICustomQuestion['correctAnswer']>;
  appTest: FormControl<ICustomQuestion['appTest']>;
};

export type CustomQuestionFormGroup = FormGroup<CustomQuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomQuestionFormService {
  createCustomQuestionFormGroup(customQuestion: CustomQuestionFormGroupInput = { id: null }): CustomQuestionFormGroup {
    const customQuestionRawValue = {
      ...this.getFormDefaults(),
      ...customQuestion,
    };
    return new FormGroup<CustomQuestionFormGroupContent>({
      id: new FormControl(
        { value: customQuestionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      question: new FormControl(customQuestionRawValue.question, {
        validators: [Validators.required],
      }),
      answer: new FormControl(customQuestionRawValue.answer),
      correctAnswer: new FormControl(customQuestionRawValue.correctAnswer),
      appTest: new FormControl(customQuestionRawValue.appTest),
    });
  }

  getCustomQuestion(form: CustomQuestionFormGroup): ICustomQuestion | NewCustomQuestion {
    return form.getRawValue() as ICustomQuestion | NewCustomQuestion;
  }

  resetForm(form: CustomQuestionFormGroup, customQuestion: CustomQuestionFormGroupInput): void {
    const customQuestionRawValue = { ...this.getFormDefaults(), ...customQuestion };
    form.reset(
      {
        ...customQuestionRawValue,
        id: { value: customQuestionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomQuestionFormDefaults {
    return {
      id: null,
    };
  }
}
