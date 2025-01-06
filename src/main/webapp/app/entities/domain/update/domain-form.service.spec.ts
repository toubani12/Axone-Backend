import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../domain.test-samples';

import { DomainFormService } from './domain-form.service';

describe('Domain Form Service', () => {
  let service: DomainFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DomainFormService);
  });

  describe('Service methods', () => {
    describe('createDomainFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDomainFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            recruiters: expect.any(Object),
            candidates: expect.any(Object),
            applications: expect.any(Object),
            employer: expect.any(Object),
          }),
        );
      });

      it('passing IDomain should create a new form with FormGroup', () => {
        const formGroup = service.createDomainFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            recruiters: expect.any(Object),
            candidates: expect.any(Object),
            applications: expect.any(Object),
            employer: expect.any(Object),
          }),
        );
      });
    });

    describe('getDomain', () => {
      it('should return NewDomain for default Domain initial value', () => {
        const formGroup = service.createDomainFormGroup(sampleWithNewData);

        const domain = service.getDomain(formGroup) as any;

        expect(domain).toMatchObject(sampleWithNewData);
      });

      it('should return NewDomain for empty Domain initial value', () => {
        const formGroup = service.createDomainFormGroup();

        const domain = service.getDomain(formGroup) as any;

        expect(domain).toMatchObject({});
      });

      it('should return IDomain', () => {
        const formGroup = service.createDomainFormGroup(sampleWithRequiredData);

        const domain = service.getDomain(formGroup) as any;

        expect(domain).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDomain should not enable id FormControl', () => {
        const formGroup = service.createDomainFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDomain should disable id FormControl', () => {
        const formGroup = service.createDomainFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
