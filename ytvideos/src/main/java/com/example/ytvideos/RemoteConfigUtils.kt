package com.example.ytvideos

import android.content.Context
import com.example.ytvideos.dataclasses.CategoriesList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

object RemoteConfigUtils {

    fun getCategoriesList(): CategoriesList? {
        val s = """{

  "data": 
     [
  {
    "category_name": "Trending Now",
    "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ftrending_now.png?alt=media&token=1504ae7c-d13f-435c-a096-e8b5d7b55d3d",
    "view_type": 1,
    "hasSubCategories": false,
    "subCategories": [
      
    ]
  },
  {
    "category_name": "Stand Up Comedy",
    "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fstand_up_comedy.png?alt=media&token=0f3a6e7d-477e-452e-ac71-89193f2cd60e",
    "view_type": 1,
    "hasSubCategories": false,
    "subCategories": [
      
    ]
  },
  {
    "category_name": "Popular Comedians",
    "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fpopular_comedians.png?alt=media&token=e9227073-a640-4744-b83a-05d4a9ed7f23",
    "view_type": 2,
    "hasSubCategories": true,
    "subCategories": [
      {
        "country": "IN",
        "sub_category_name": "Zakir Khan",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fzakir_khan.png?alt=media&token=ce7f121d-5af8-4a16-9521-d548d254a921"
      },
      {
        "country": "IN",
        "sub_category_name": "Kenny Sebastian",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fkenny_sebastian.png?alt=media&token=e79fe6cd-6152-4093-a1d0-1877318769ca"
      },
      {
        "country": "IN",
        "sub_category_name": "Abhishek Upmanyu",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fabhishek_upmanyu.png?alt=media&token=20f15216-0b84-4dab-a001-1a39a327717e"
      },
      {
        "country": "IN",
        "sub_category_name": "Anubhav Singh Bassi",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fanubhav_singh_bassi.png?alt=media&token=23d8eb46-ff0b-4ff1-9750-e9c699c5c42b"
      },
      {
        "country": "IN",
        "sub_category_name": "Abish Mathew",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fabish_mathew.png?alt=media&token=07bf96af-102c-48e1-be44-4de20f9254d2"
      },
      {
        "country": "IN",
        "sub_category_name": "Samay Raina",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fsamay_raina.png?alt=media&token=967d5401-5149-4420-835b-9cc45af7d8cf"
      },
      {
        "country": "IN",
        "sub_category_name": "Aakash Gupta",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Faakash_gupta.png?alt=media&token=95a9e43d-e6db-4165-b68d-0b5292cb6eba"
      },
      {
        "country": "IN",
        "sub_category_name": "Ravi Gupta",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fravi_gupta.png?alt=media&token=9218724c-92d8-44e8-924a-13c67df4c663"
      },
      {
        "country": "IN",
        "sub_category_name": "Gaurav Kapoor",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fgaurav_kapoor.png?alt=media&token=2187cd9d-7bf9-49d5-8587-829edae2f271"
      },
      {
        "country": "IN",
        "sub_category_name": "Atul Khatri",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fatul_khatri.png?alt=media&token=b43f4c1b-7d84-4f99-a16f-9f5187e09cb5"
      },
      {
        "country": "US",
        "sub_category_name": "Gabriel Iglesias",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fgabriel_iglesias.png?alt=media&token=53ab004b-2889-4b77-8098-083ab993604e"
      },
      {
        "country": "US",
        "sub_category_name": "Mark Normand",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fmark_normand.png?alt=media&token=d5442720-9586-4a49-b41a-2aa30966bf95"
      },
      {
        "country": "US",
        "sub_category_name": "Theo Von",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Ftheo_von.png?alt=media&token=6ba18e61-0294-4847-a5ad-fc37c1c173b6"
      },
      {
        "country": "US",
        "sub_category_name": "Tom Segura",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Ftom_segura.png?alt=media&token=92459bac-fc7a-41f6-aeb2-4729a52080f4"
      },
      {
        "country": "US",
        "sub_category_name": "Bert Kreischer",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fbert_kreischer.png?alt=media&token=6894489a-a9e1-4b6e-82a6-bb79069d206b"
      },
      {
        "country": "US",
        "sub_category_name": "Jo Koy",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fjo_koy.png?alt=media&token=75a54321-e546-4e0f-9edd-507afa4792f2"
      },
      {
        "country": "US",
        "sub_category_name": "Liz Miele",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fliz_miele.png?alt=media&token=3f282f98-bcc5-40aa-967c-9015ed828e48"
      },
      {
        "country": "US",
        "sub_category_name": "Sam Morril",
        "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_comedians%2Fsam_morril.png?alt=media&token=63c1941d-80b3-4e4e-8c2a-54ba57695d31"
      }
    ]
  },
  {
    "category_name": "Drama Collection",
    "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fdrama_collection.png?alt=media&token=59104eea-8749-465a-9f5e-51abd81433aa",
    "view_type": 3,
    "hasSubCategories": false,
    "subCategories": [
      
    ]
  }
  
]
 
}

"""

//
////
//        {
//            "category_name": "Travel & Vlogs",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ftravel_vlogs.png?alt=media&token=6bf89dbc-f450-4c7e-9e0c-1fda04642431",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Top Music Picks",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ftop_music_picks.png?alt=media&token=5ad16153-a24b-43ca-b9ef-ea528ce685ff",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Music Genres",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fmusic_genres.png?alt=media&token=49cf4c1e-c418-4441-a753-ca319866056d",
//            "view_type": "2",
//            "hasSubCategories": true,
//            "subCategories": [
//            {
//                "country": "IN",
//                "sub_category_name": "Bollywood",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fbollywood.png?alt=media&token=e338e9fa-690f-4233-9359-3d7efc8efa10"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Punjabi",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fpunjabi.png?alt=media&token=0b78e69d-40c7-40c2-853d-63f90c4e4fd2"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Bhojpuri",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fbhojpuri.png?alt=media&token=606a1215-a367-4fe1-b641-d51e2a0f7def"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Tamil",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Ftamil.png?alt=media&token=ed8d3844-614c-4d23-b52b-76dee023e1dd"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Telugu",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Ftelugu.png?alt=media&token=c6999045-eedb-4b66-8f12-8145767a928f"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Gujrati",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fgujrati.png?alt=media&token=f1263c1a-a194-4c96-af90-64824c483875"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Marathi",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fmarathi.png?alt=media&token=20ebe15e-4350-41bb-8428-364a8f045ada"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Rajasthani",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Frajasthani.png?alt=media&token=511e650a-8897-4a8a-a864-3203d3f1f0b0"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Haryanvi",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fharyanvi.png?alt=media&token=9568b54c-a895-4a46-9b51-8ff05a785ee2"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Devotional",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fmusic_genres%2Fdevotional.png?alt=media&token=a6eac6d5-4688-4bf3-a51e-e828c2ef7597"
//            }
//            ]
//        },
//        {
//            "category_name": "Wildlife & Nature",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fwildlife_nature.png?alt=media&token=b1a694fd-9bb8-471f-8061-949c225f5348",
//            "view_type": "3",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Talk Shows & Podcasts",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ftalk_shows_podcasts.png?alt=media&token=bd6003a2-e8ab-445d-a4e1-a32cfc41086f",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Popular Podcasters",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fpopular_podcasters.png?alt=media&token=e24a0e60-8bb8-4574-ad5e-afb8da9cc299",
//            "view_type": "2",
//            "hasSubCategories": true,
//            "subCategories": [
//            {
//                "country": "IN",
//                "sub_category_name": "Ranveer Allahbadia",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Franveer_allahbadia.png?alt=media&token=00e928f6-f4b2-4985-8f26-b1cd21072f72"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Raj Shamani",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fraj_shamani.png?alt=media&token=42b610d2-0fd2-4b5c-8a99-53f3bbcc6841"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Nikhil Kamath",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fnikhil_kamath.png?alt=media&token=857da9a3-b952-44f7-bf9f-efd4d3fd80af"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Prakhar Gupta",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fprakhar_gupta.png?alt=media&token=52db9458-67cc-41b1-a0bc-ce0289ced78d"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Vinamre Kasanaa",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fvinamre_kasanaa.png?alt=media&token=99bae59e-fddc-46fe-8084-ce372e8e44c2"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Varun Mayya",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fvarun_mayya.png?alt=media&token=8958ccca-cf5c-45f2-a710-ede689511d47"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Sharan Hegde",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_podcasters%2Fsharan_hegde.png?alt=media&token=bba5e569-f2eb-44be-bb9f-df894031a2a1"
//            }
//            ]
//        },
//        {
//            "category_name": "Food & Recipes",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ffood%20_recipes.png?alt=media&token=9125c034-78bd-4576-92e1-571d4f4ada15",
//            "view_type": "3",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Knowledge & Unknown Facts",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fknowledge_unknown_facts.png?alt=media&token=602bf4b9-fde1-403e-a0eb-68fc1418cdee",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Technology & Gadgets",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ftechnology_gadgets.png?alt=media&token=799e56fd-6425-42aa-8bee-5ca531e01e05",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Fitness & Health",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Ffitness_health.png?alt=media&token=c72b81ef-ff61-40a0-a69e-3f154b6db21d",
//            "view_type": "1",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        },
//        {
//            "category_name": "Popular Influncers",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fpopular_influncers.png?alt=media&token=738b1280-5f54-4e16-a1f4-b1c228e6ac69",
//            "view_type": "2",
//            "hasSubCategories": true,
//            "subCategories": [
//            {
//                "country": "IN",
//                "sub_category_name": "Gaurav Chaudhary",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fgaurav_chaudhary.png?alt=media&token=75833e86-df2a-4432-88c9-699aff2a7db8"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Sandeep Maheshwari",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fsandeep_maheshwari.png?alt=media&token=a4aa3b55-dee7-4e55-bbf5-a0f5a0bbc27a"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Bhuvan Bam",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fbhuvan_bam.png?alt=media&token=3fc84b8e-7526-4a39-b9d5-758819ff184e"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Amit Bhadana",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Famit_bhadana.png?alt=media&token=993fd576-f48d-4edb-b2eb-2de3b33275bb"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Carry Minati",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fcarry_minati.png?alt=media&token=da879e3b-0269-4593-93d0-9b340a0aad4c"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Ashish Chanchlani",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fashish_chanchlani.png?alt=media&token=b6874220-01c0-4af8-9039-aa38609b64fc"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Allena Rais",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fallena_rais.png?alt=media&token=27a759cc-98d6-46b5-9e7f-64aaca738c7f"
//            },
//            {
//                "country": "IN",
//                "sub_category_name": "Prajakta Koli",
//                "sub_category_img": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fpopular_influncers%2Fprajakta_koli.png?alt=media&token=6b615e53-7cad-4187-89b2-8a330d4c9b31"
//            }
//            ]
//        },
//        {
//            "category_name": "Web Series - Episodes",
//            "category_icon": "https://firebasestorage.googleapis.com/v0/b/avx-player.appspot.com/o/online_videos%2Fcategory_icons%2Fweb_series.png?alt=media&token=1ba22295-0066-4a3e-8a25-72fe847b32bc",
//            "view_type": "3",
//            "hasSubCategories": false,
//            "subCategories": [
//
//            ]
//        }
        return try {

                val jsonObject = JSONObject(s)
                val gson = Gson()
                val type =
                    object : TypeToken<CategoriesList?>() {}.type
                return gson.fromJson<CategoriesList>(jsonObject.toString(), type)


        } catch (e: java.lang.Exception) {
            null
        }
    }
}