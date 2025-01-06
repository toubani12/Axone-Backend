import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../application.test-samples';

import { ApplicationFormService } from './application-form.service';

describe('Application Form Service', () => {
  let service: ApplicationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationFormService);
  });

  describe('Service methods', () => {
    describe('createApplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            numberOfCandidates: expect.any(Object),
            paymentAmount: expect.any(Object),
            recruiterIncomeRate: expect.any(Object),
            candidateIncomeRate: expect.any(Object),
            deadline: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            openedAt: expect.any(Object),
            closedAt: expect.any(Object),
            doneAt: expect.any(Object),
            contractTypes: expect.any(Object),
            contractTemplates: expect.any(Object),
            criteria: expect.any(Object),
            domains: expect.any(Object),
            employer: expect.any(Object),
            recruiters: expect.any(Object),
            candidates: expect.any(Object),
          }),
        );
      });

      it('passing IApplication should create a new form with FormGroup', () => {
        const formGroup = service.createApplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            numberOfCandidates: expect.any(Object),
            paymentAmount: expect.any(Object),
            recruiterIncomeRate: expect.any(Object),
            candidateIncomeRate: expect.any(Object),
            deadline: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            openedAt: expect.any(Object),
            closedAt: expect.any(Object),
            doneAt: expect.any(Object),
            contractTypes: expect.any(Object),
            contractTemplates: expect.any(Object),
            criteria: expect.any(Object),
            domains: expect.any(Object),
            employer: expect.any(Object),
            recruiters: expect.any(Object),
            candidates: expect.any(Object),
          }),
        );
      });
    });

    describe('getApplication', () => {
      it('should return NewApplication for default Application initial value', () => {
        const formGroup = service.createApplicationFormGroup(sampleWithNewData);

        const application = service.getApplication(formGroup) as any;

        expect(application).toMatchObject(sampleWithNewData);
      });

      it('should return NewApplication for empty Application initial value', () => {
        const formGroup = service.createApplicationFormGroup();

        const application = service.getApplication(formGroup) as any;

        expect(application).toMatchObject({});
      });

      it('should return IApplication', () => {
        const formGroup = service.createApplicationFormGroup(sampleWithRequiredData);

        const application = service.getApplication(formGroup) as any;

        expect(application).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApplication should not enable id FormControl', () => {
        const formGroup = service.createApplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApplication should disable id FormControl', () => {
        const formGroup = service.createApplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
