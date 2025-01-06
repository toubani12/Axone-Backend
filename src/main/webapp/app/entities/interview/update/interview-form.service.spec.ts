import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../interview.test-samples';

import { InterviewFormService } from './interview-form.service';

describe('Interview Form Service', () => {
  let service: InterviewFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterviewFormService);
  });

  describe('Service methods', () => {
    describe('createInterviewFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInterviewFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            entryLink: expect.any(Object),
            invitationLink: expect.any(Object),
            interviewResult: expect.any(Object),
            rate: expect.any(Object),
            startedAt: expect.any(Object),
            endedAt: expect.any(Object),
            comments: expect.any(Object),
            attendee: expect.any(Object),
            application: expect.any(Object),
          }),
        );
      });

      it('passing IInterview should create a new form with FormGroup', () => {
        const formGroup = service.createInterviewFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            entryLink: expect.any(Object),
            invitationLink: expect.any(Object),
            interviewResult: expect.any(Object),
            rate: expect.any(Object),
            startedAt: expect.any(Object),
            endedAt: expect.any(Object),
            comments: expect.any(Object),
            attendee: expect.any(Object),
            application: expect.any(Object),
          }),
        );
      });
    });

    describe('getInterview', () => {
      it('should return NewInterview for default Interview initial value', () => {
        const formGroup = service.createInterviewFormGroup(sampleWithNewData);

        const interview = service.getInterview(formGroup) as any;

        expect(interview).toMatchObject(sampleWithNewData);
      });

      it('should return NewInterview for empty Interview initial value', () => {
        const formGroup = service.createInterviewFormGroup();

        const interview = service.getInterview(formGroup) as any;

        expect(interview).toMatchObject({});
      });

      it('should return IInterview', () => {
        const formGroup = service.createInterviewFormGroup(sampleWithRequiredData);

        const interview = service.getInterview(formGroup) as any;

        expect(interview).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInterview should not enable id FormControl', () => {
        const formGroup = service.createInterviewFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInterview should disable id FormControl', () => {
        const formGroup = service.createInterviewFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
