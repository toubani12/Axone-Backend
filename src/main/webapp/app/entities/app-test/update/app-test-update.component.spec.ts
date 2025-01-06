import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppTestType } from 'app/entities/app-test-type/app-test-type.model';
import { AppTestTypeService } from 'app/entities/app-test-type/service/app-test-type.service';
import { AppTestService } from '../service/app-test.service';
import { IAppTest } from '../app-test.model';
import { AppTestFormService } from './app-test-form.service';

import { AppTestUpdateComponent } from './app-test-update.component';

describe('AppTest Management Update Component', () => {
  let comp: AppTestUpdateComponent;
  let fixture: ComponentFixture<AppTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appTestFormService: AppTestFormService;
  let appTestService: AppTestService;
  let appTestTypeService: AppTestTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppTestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AppTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appTestFormService = TestBed.inject(AppTestFormService);
    appTestService = TestBed.inject(AppTestService);
    appTestTypeService = TestBed.inject(AppTestTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppTestType query and add missing value', () => {
      const appTest: IAppTest = { id: 456 };
      const types: IAppTestType[] = [{ id: 27174 }];
      appTest.types = types;

      const appTestTypeCollection: IAppTestType[] = [{ id: 29474 }];
      jest.spyOn(appTestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: appTestTypeCollection })));
      const additionalAppTestTypes = [...types];
      const expectedCollection: IAppTestType[] = [...additionalAppTestTypes, ...appTestTypeCollection];
      jest.spyOn(appTestTypeService, 'addAppTestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appTest });
      comp.ngOnInit();

      expect(appTestTypeService.query).toHaveBeenCalled();
      expect(appTestTypeService.addAppTestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        appTestTypeCollection,
        ...additionalAppTestTypes.map(expect.objectContaining),
      );
      expect(comp.appTestTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appTest: IAppTest = { id: 456 };
      const type: IAppTestType = { id: 7902 };
      appTest.types = [type];

      activatedRoute.data = of({ appTest });
      comp.ngOnInit();

      expect(comp.appTestTypesSharedCollection).toContain(type);
      expect(comp.appTest).toEqual(appTest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTest>>();
      const appTest = { id: 123 };
      jest.spyOn(appTestFormService, 'getAppTest').mockReturnValue(appTest);
      jest.spyOn(appTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appTest }));
      saveSubject.complete();

      // THEN
      expect(appTestFormService.getAppTest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appTestService.update).toHaveBeenCalledWith(expect.objectContaining(appTest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTest>>();
      const appTest = { id: 123 };
      jest.spyOn(appTestFormService, 'getAppTest').mockReturnValue({ id: null });
      jest.spyOn(appTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appTest }));
      saveSubject.complete();

      // THEN
      expect(appTestFormService.getAppTest).toHaveBeenCalled();
      expect(appTestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTest>>();
      const appTest = { id: 123 };
      jest.spyOn(appTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appTestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppTestType', () => {
      it('Should forward to appTestTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appTestTypeService, 'compareAppTestType');
        comp.compareAppTestType(entity, entity2);
        expect(appTestTypeService.compareAppTestType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
