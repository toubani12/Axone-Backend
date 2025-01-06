import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../custom-question.test-samples';

import { CustomQuestionFormService } from './custom-question-form.service';

describe('CustomQuestion Form Service', () => {
  let service: CustomQuestionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomQuestionFormService);
  });

  describe('Service methods', () => {
    describe('createCustomQuestionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomQuestionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            question: expect.any(Object),
            answer: expect.any(Object),
            correctAnswer: expect.any(Object),
            appTest: expect.any(Object),
          }),
        );
      });

      it('passing ICustomQuestion should create a new form with FormGroup', () => {
        const formGroup = service.createCustomQuestionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            question: expect.any(Object),
            answer: expect.any(Object),
            correctAnswer: expect.any(Object),
            appTest: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomQuestion', () => {
      it('should return NewCustomQuestion for default CustomQuestion initial value', () => {
        const formGroup = service.createCustomQuestionFormGroup(sampleWithNewData);

        const customQuestion = service.getCustomQuestion(formGroup) as any;

        expect(customQuestion).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomQuestion for empty CustomQuestion initial value', () => {
        const formGroup = service.createCustomQuestionFormGroup();

        const customQuestion = service.getCustomQuestion(formGroup) as any;

        expect(customQuestion).toMatchObject({});
      });

      it('should return ICustomQuestion', () => {
        const formGroup = service.createCustomQuestionFormGroup(sampleWithRequiredData);

        const customQuestion = service.getCustomQuestion(formGroup) as any;

        expect(customQuestion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomQuestion should not enable id FormControl', () => {
        const formGroup = service.createCustomQuestionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomQuestion should disable id FormControl', () => {
        const formGroup = service.createCustomQuestionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
