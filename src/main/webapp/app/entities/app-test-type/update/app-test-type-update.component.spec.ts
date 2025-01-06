import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppTest } from 'app/entities/app-test/app-test.model';
import { AppTestService } from 'app/entities/app-test/service/app-test.service';
import { AppTestTypeService } from '../service/app-test-type.service';
import { IAppTestType } from '../app-test-type.model';
import { AppTestTypeFormService } from './app-test-type-form.service';

import { AppTestTypeUpdateComponent } from './app-test-type-update.component';

describe('AppTestType Management Update Component', () => {
  let comp: AppTestTypeUpdateComponent;
  let fixture: ComponentFixture<AppTestTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appTestTypeFormService: AppTestTypeFormService;
  let appTestTypeService: AppTestTypeService;
  let appTestService: AppTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppTestTypeUpdateComponent],
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
      .overrideTemplate(AppTestTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppTestTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appTestTypeFormService = TestBed.inject(AppTestTypeFormService);
    appTestTypeService = TestBed.inject(AppTestTypeService);
    appTestService = TestBed.inject(AppTestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppTest query and add missing value', () => {
      const appTestType: IAppTestType = { id: 456 };
      const appTests: IAppTest[] = [{ id: 1932 }];
      appTestType.appTests = appTests;

      const appTestCollection: IAppTest[] = [{ id: 29565 }];
      jest.spyOn(appTestService, 'query').mockReturnValue(of(new HttpResponse({ body: appTestCollection })));
      const additionalAppTests = [...appTests];
      const expectedCollection: IAppTest[] = [...additionalAppTests, ...appTestCollection];
      jest.spyOn(appTestService, 'addAppTestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appTestType });
      comp.ngOnInit();

      expect(appTestService.query).toHaveBeenCalled();
      expect(appTestService.addAppTestToCollectionIfMissing).toHaveBeenCalledWith(
        appTestCollection,
        ...additionalAppTests.map(expect.objectContaining),
      );
      expect(comp.appTestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appTestType: IAppTestType = { id: 456 };
      const appTest: IAppTest = { id: 10737 };
      appTestType.appTests = [appTest];

      activatedRoute.data = of({ appTestType });
      comp.ngOnInit();

      expect(comp.appTestsSharedCollection).toContain(appTest);
      expect(comp.appTestType).toEqual(appTestType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTestType>>();
      const appTestType = { id: 123 };
      jest.spyOn(appTestTypeFormService, 'getAppTestType').mockReturnValue(appTestType);
      jest.spyOn(appTestTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appTestType }));
      saveSubject.complete();

      // THEN
      expect(appTestTypeFormService.getAppTestType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appTestTypeService.update).toHaveBeenCalledWith(expect.objectContaining(appTestType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTestType>>();
      const appTestType = { id: 123 };
      jest.spyOn(appTestTypeFormService, 'getAppTestType').mockReturnValue({ id: null });
      jest.spyOn(appTestTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTestType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appTestType }));
      saveSubject.complete();

      // THEN
      expect(appTestTypeFormService.getAppTestType).toHaveBeenCalled();
      expect(appTestTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppTestType>>();
      const appTestType = { id: 123 };
      jest.spyOn(appTestTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appTestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appTestTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppTest', () => {
      it('Should forward to appTestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appTestService, 'compareAppTest');
        comp.compareAppTest(entity, entity2);
        expect(appTestService.compareAppTest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
