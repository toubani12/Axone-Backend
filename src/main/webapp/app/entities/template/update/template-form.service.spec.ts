import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../template.test-samples';

import { TemplateFormService } from './template-form.service';

describe('Template Form Service', () => {
  let service: TemplateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemplateFormService);
  });

  describe('Service methods', () => {
    describe('createTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTemplateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            standard: expect.any(Object),
            docLink: expect.any(Object),
            owner: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });

      it('passing ITemplate should create a new form with FormGroup', () => {
        const formGroup = service.createTemplateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            standard: expect.any(Object),
            docLink: expect.any(Object),
            owner: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });
    });

    describe('getTemplate', () => {
      it('should return NewTemplate for default Template initial value', () => {
        const formGroup = service.createTemplateFormGroup(sampleWithNewData);

        const template = service.getTemplate(formGroup) as any;

        expect(template).toMatchObject(sampleWithNewData);
      });

      it('should return NewTemplate for empty Template initial value', () => {
        const formGroup = service.createTemplateFormGroup();

        const template = service.getTemplate(formGroup) as any;

        expect(template).toMatchObject({});
      });

      it('should return ITemplate', () => {
        const formGroup = service.createTemplateFormGroup(sampleWithRequiredData);

        const template = service.getTemplate(formGroup) as any;

        expect(template).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITemplate should not enable id FormControl', () => {
        const formGroup = service.createTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTemplate should disable id FormControl', () => {
        const formGroup = service.createTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
