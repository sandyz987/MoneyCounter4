package com.example.moneycounter4.model

import android.app.Application
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.utils.ResourceGetter
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel

class TypeIndex {

    companion object {
        fun getAllType():ArrayList<TypeItem>{
            val a = ArrayList<TypeItem>()
            val list = typeS.split("\n")
            for (s : String in list){
                val id = ResourceGetter.getResourceId(s.replace(".png",""))
                if(id != 0){
                    a.add((TypeItem("",id)))
                }
            }
            return a
        }

        fun getInTypeInit():ArrayList<TypeItem>{
            val a = ArrayList<TypeItem>()
            val list = typeIn.split("\n")
            for (i in list.indices step 2){
                val id = ResourceGetter.getResourceId(list[i].replace(".png",""))
                if(id != 0){
                    a.add((TypeItem(list[i+1],id)))
                }
            }
            return a
        }


        fun getOutTypeInit():ArrayList<TypeItem>{
            val a = ArrayList<TypeItem>()
            val list = typeOut.split("\n")
            for (i in list.indices step 2){
                val id = ResourceGetter.getResourceId(list[i].replace(".png",""))
                if(id != 0){
                    a.add((TypeItem(list[i+1],id)))
                }
            }
            return a
        }
        @JvmStatic
        fun findTypePicIdByName(vm:MainViewModel,s : String):Int{
            for(e:TypeItem in vm.typeListIn){
                if(s == e.name){
                    return e.resId
                }
            }
            for(e:TypeItem in vm.typeListOut){
                if(s == e.name){
                    return e.resId
                }
            }
            return 0
        }

        val typeOut = "category_e_beauty_normal.png\n" +
                "美容\n" +
                "category_e_books_normal.png\n" +
                "书籍\n" +
                "category_e_car_normal.png\n" +
                "汽车\n" +
                "category_e_catering_normal.png\n" +
                "餐饮\n" +
                "category_e_child_normal.png\n" +
                "子女\n" +
                "category_e_commodity_normal.png\n" +
                "日用\n" +
                "category_e_communicate_normal.png\n" +
                "通讯\n" +
                "category_e_digital_normal.png\n" +
                "数码\n" +
                "category_e_donate_normal.png\n" +
                "捐献\n" +
                "category_e_dress_normal.png\n" +
                "穿戴\n" +
                "category_e_elder_normal.png\n" +
                "老人\n" +
                "category_e_entertainmente_normal.png\n" +
                "娱乐\n" +
                "category_e_express_normal.png\n" +
                "表演\n" +
                "category_e_friend_normal.png\n" +
                "朋友\n" +
                "category_e_fruite_normal.png\n" +
                "水果\n" +
                "category_e_gift_normal.png\n" +
                "礼品\n" +
                "category_e_home_normal.png\n" +
                "家居\n" +
                "category_e_house_normal.png\n" +
                "住房\n" +
                "category_e_lottery_normal.png\n" +
                "彩票\n" +
                "category_e_medical_normal.png\n" +
                "医疗\n" +
                "category_e_money_normal.png\n" +
                "礼金\n" +
                "category_e_office_normal.png\n" +
                "办公\n" +
                "category_e_pet_normal.png\n" +
                "宠物\n" +
                "category_e_repair_normal.png\n" +
                "维修\n" +
                "category_e_shopping_normal.png\n" +
                "购物\n" +
                "category_e_smoke_normal.png\n" +
                "烟酒\n" +
                "category_e_snack_normal.png\n" +
                "零食\n" +
                "category_e_social_normal.png\n" +
                "社交\n" +
                "category_e_sport_normal.png\n" +
                "体育\n" +
                "category_e_study_normal.png\n" +
                "学习\n" +
                "category_e_traffic_normal.png\n" +
                "公交\n" +
                "category_e_travel_normal.png\n" +
                "旅行\n" +
                "category_e_vegetable_normal.png\n" +
                "菜市"


        val typeIn = "category_i_finance_normal.png\n" +
                "理财\n" +
                "category_i_money_normal.png\n" +
                "礼金\n" +
                "category_i_other_normal.png\n" +
                "其他\n" +
                "category_i_parttimework_normal.png\n" +
                "兼职\n" +
                "category_i_wage_normal.png\n" +
                "工资"

        val typeS = "category_custom_normal.png\n" +
                "category_e_beauty_normal.png\n" +
                "category_e_books_normal.png\n" +
                "category_e_car_normal.png\n" +
                "category_e_catering_normal.png\n" +
                "category_e_child_normal.png\n" +
                "category_e_commodity_normal.png\n" +
                "category_e_communicate_normal.png\n" +
                "category_e_digital_normal.png\n" +
                "category_e_donate_normal.png\n" +
                "category_e_dress_normal.png\n" +
                "category_e_elder_normal.png\n" +
                "category_e_entertainmente_normal.png\n" +
                "category_e_express_normal.png\n" +
                "category_e_friend_normal.png\n" +
                "category_e_fruite_normal.png\n" +
                "category_e_gift_normal.png\n" +
                "category_e_home_normal.png\n" +
                "category_e_house_normal.png\n" +
                "category_e_lottery_normal.png\n" +
                "category_e_medical_normal.png\n" +
                "category_e_money_normal.png\n" +
                "category_e_office_normal.png\n" +
                "category_e_pet_normal.png\n" +
                "category_e_repair_normal.png\n" +
                "category_e_shopping_normal.png\n" +
                "category_e_smoke_normal.png\n" +
                "category_e_snack_normal.png\n" +
                "category_e_social_normal.png\n" +
                "category_e_sport_normal.png\n" +
                "category_e_study_normal.png\n" +
                "category_e_traffic_normal.png\n" +
                "category_e_travel_normal.png\n" +
                "category_e_vegetable_normal.png\n" +
                "category_i_finance_normal.png\n" +
                "category_i_money_normal.png\n" +
                "category_i_other_normal.png\n" +
                "category_i_parttimework_normal.png\n" +
                "category_i_wage_normal.png\n" +
                "cc_catering_apple.png\n" +
                "cc_catering_banana.png\n" +
                "cc_catering_beer.png\n" +
                "cc_catering_birthday_cake.png\n" +
                "cc_catering_bottle.png\n" +
                "cc_catering_cake.png\n" +
                "cc_catering_chicken.png\n" +
                "cc_catering_coffee.png\n" +
                "cc_catering_drumstick.png\n" +
                "cc_catering_hamburg.png\n" +
                "cc_catering_hot_pot.png\n" +
                "cc_catering_ice_cream.png\n" +
                "cc_catering_ice_lolly.png\n" +
                "cc_catering_noodle.png\n" +
                "cc_catering_red_wine.png\n" +
                "cc_catering_rice.png\n" +
                "cc_catering_seafood.png\n" +
                "cc_catering_skewer.png\n" +
                "cc_catering_sushi.png\n" +
                "cc_catering_tea.png\n" +
                "cc_entertainmente_archery.png\n" +
                "cc_entertainmente_badminton.png\n" +
                "cc_entertainmente_baseball.png\n" +
                "cc_entertainmente_basketball.png\n" +
                "cc_entertainmente_billiards.png\n" +
                "cc_entertainmente_bowling.png\n" +
                "cc_entertainmente_chess.png\n" +
                "cc_entertainmente_climbing.png\n" +
                "cc_entertainmente_gambling.png\n" +
                "cc_entertainmente_game.png\n" +
                "cc_entertainmente_movies.png\n" +
                "cc_entertainmente_ping_pong.png\n" +
                "cc_entertainmente_poker.png\n" +
                "cc_entertainmente_racing.png\n" +
                "cc_entertainmente_roller_skating.png\n" +
                "cc_entertainmente_sailing.png\n" +
                "cc_entertainmente_skiing.png\n" +
                "cc_entertainmente_swimming.png\n" +
                "cc_entertainmente_whirligig.png\n" +
                "cc_family_baby.png\n" +
                "cc_family_baby_carriage.png\n" +
                "cc_family_dog.png\n" +
                "cc_family_feeding_bottle.png\n" +
                "cc_family_nipple.png\n" +
                "cc_family_pet_food.png\n" +
                "cc_family_pet_home.png\n" +
                "cc_family_teddy_bear.png\n" +
                "cc_family_toy_duck.png\n" +
                "cc_family_wooden_horse.png\n" +
                "cc_fitness_barbell.png\n" +
                "cc_fitness_bodybuilding.png\n" +
                "cc_fitness_boxing.png\n" +
                "cc_fitness_dumbbell.png\n" +
                "cc_fitness_elliptical_machine.png\n" +
                "cc_fitness_fitball.png\n" +
                "cc_fitness_hand_muscle_developer.png\n" +
                "cc_fitness_running.png\n" +
                "cc_fitness_sit_in.png\n" +
                "cc_fitness_skating.png\n" +
                "cc_fitness_treadmills.png\n" +
                "cc_fitness_weightlifting.png\n" +
                "cc_home_air_conditioner.png\n" +
                "cc_home_bathtub.png\n" +
                "cc_home_bed.png\n" +
                "cc_home_bread_machine.png\n" +
                "cc_home_bulb.png\n" +
                "cc_home_hair_drier.png\n" +
                "cc_home_microwave_oven.png\n" +
                "cc_home_refrigerator.png\n" +
                "cc_home_renovate.png\n" +
                "cc_home_sofa.png\n" +
                "cc_home_tools.png\n" +
                "cc_home_w_and_e.png\n" +
                "cc_home_wardrobe.png\n" +
                "cc_home_washing_machine.png\n" +
                "cc_home_water.png\n" +
                "cc_income_1.png\n" +
                "cc_income_10.png\n" +
                "cc_income_2.png\n" +
                "cc_income_3.png\n" +
                "cc_income_4.png\n" +
                "cc_income_5.png\n" +
                "cc_income_6.png\n" +
                "cc_income_7.png\n" +
                "cc_income_8.png\n" +
                "cc_income_9.png\n" +
                "cc_life_bath.png\n" +
                "cc_life_buddha.png\n" +
                "cc_life_candlelight.png\n" +
                "cc_life_date.png\n" +
                "cc_life_holiday.png\n" +
                "cc_life_hotel.png\n" +
                "cc_life_moods_of_love.png\n" +
                "cc_life_spa.png\n" +
                "cc_life_sunbath.png\n" +
                "cc_life_tea.png\n" +
                "cc_life_tent.png\n" +
                "cc_life_trip.png\n" +
                "cc_medical_child.png\n" +
                "cc_medical_ct.png\n" +
                "cc_medical_doctor.png\n" +
                "cc_medical_echometer.png\n" +
                "cc_medical_injection.png\n" +
                "cc_medical_medicine.png\n" +
                "cc_medical_pregnant.png\n" +
                "cc_medical_tooth.png\n" +
                "cc_medical_transfusion.png\n" +
                "cc_medical_wheelchair.png\n" +
                "cc_office_clip.png\n" +
                "cc_office_computer.png\n" +
                "cc_office_desk.png\n" +
                "cc_office_drawing_board.png\n" +
                "cc_office_keyboard.png\n" +
                "cc_office_mouse.png\n" +
                "cc_office_pen_container.png\n" +
                "cc_office_pen_ruler.png\n" +
                "cc_office_printer.png\n" +
                "cc_other_crown.png\n" +
                "cc_other_diamond.png\n" +
                "cc_other_firecracker.png\n" +
                "cc_other_flag.png\n" +
                "cc_other_lantern.png\n" +
                "cc_other_memorial_day.png\n" +
                "cc_other_zongzi.png\n" +
                "cc_personal_clap.png\n" +
                "cc_personal_facial.png\n" +
                "cc_personal_favourite.png\n" +
                "cc_personal_friend.png\n" +
                "cc_personal_handshake.png\n" +
                "cc_personal_love.png\n" +
                "cc_personal_marry.png\n" +
                "cc_personal_money.png\n" +
                "cc_personal_pc.png\n" +
                "cc_personal_phone.png\n" +
                "cc_shopping_baby.png\n" +
                "cc_shopping_belt.png\n" +
                "cc_shopping_bikini.png\n" +
                "cc_shopping_boots.png\n" +
                "cc_shopping_camera.png\n" +
                "cc_shopping_cosmetics.png\n" +
                "cc_shopping_earrings.png\n" +
                "cc_shopping_eye_shadow.png\n" +
                "cc_shopping_flower.png\n" +
                "cc_shopping_flowerpot.png\n" +
                "cc_shopping_glasses.png\n" +
                "cc_shopping_hand_cream.png\n" +
                "cc_shopping_hat.png\n" +
                "cc_shopping_headset.png\n" +
                "cc_shopping_high_heels.png\n" +
                "cc_shopping_kettle.png\n" +
                "cc_shopping_knickers.png\n" +
                "cc_shopping_lipstick.png\n" +
                "cc_shopping_mascara.png\n" +
                "cc_shopping_necklace.png\n" +
                "cc_shopping_necktie.png\n" +
                "cc_shopping_package.png\n" +
                "cc_shopping_ring.png\n" +
                "cc_shopping_shopping_trolley.png\n" +
                "cc_shopping_skirt.png\n" +
                "cc_shopping_tie.png\n" +
                "cc_shopping_toiletries.png\n" +
                "cc_shopping_trousers.png\n" +
                "cc_shopping_watch.png\n" +
                "cc_study_blackboard.png\n" +
                "cc_study_book.png\n" +
                "cc_study_calculator.png\n" +
                "cc_study_guitars.png\n" +
                "cc_study_hat.png\n" +
                "cc_study_lamp.png\n" +
                "cc_study_penpaper.png\n" +
                "cc_study_penruler.png\n" +
                "cc_study_piano.png\n" +
                "cc_study_school.png\n" +
                "cc_traffic_bike.png\n" +
                "cc_traffic_car.png\n" +
                "cc_traffic_car_insurance.png\n" +
                "cc_traffic_car_wash.png\n" +
                "cc_traffic_charge.png\n" +
                "cc_traffic_double_deck_bus.png\n" +
                "cc_traffic_expressway.png\n" +
                "cc_traffic_gasoline.png\n" +
                "cc_traffic_motorbike.png\n" +
                "cc_traffic_parking.png\n" +
                "cc_traffic_plane.png\n" +
                "cc_traffic_refuel.png\n" +
                "cc_traffic_ship.png\n" +
                "cc_traffic_taxi.png\n" +
                "cc_traffic_train.png\n" +
                "cc_traffic_truck.png"
    }



}